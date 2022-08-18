package com.unikey.android.database.cloud

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.unikey.android.*
import com.unikey.android.objects.*
import com.unikey.android.ui.studymaterials.adapters.DownloadStatus
import com.unikey.android.ui.studymaterials.ui.UploadStatus
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

private const val ggh =0

class CloudStorage {

    // Instance of FireStore
    private val db = Firebase.firestore

    private val mediaStorage = Firebase.storage

    // Instance of Authentication
    private val auth = Authentication()

    // User_details document reference
    private val usersRef = db.collection("users_details")


    companion object{

        private val user = User(
            sem = 0,
        )

        private var userRegNo: Long = DefaultValue                          // Globally accessible userRegNo

        private var userInfo_: MutableLiveData<User>? = MutableLiveData()   // Globally accessible UserInfo
        val userInfo
            get() = userInfo_

        private var localChapterList = mutableListOf<String>()

    }


    /** Updates the [userRegNo] of the user */
    fun updateRegNo(regNo: Long){
        userRegNo = regNo
        Log.d(TAG, "updateRegNo: $userRegNo")
        getUserInfo()
    }

    fun printLog(){
        Log.d("BBB", "printLog: $userRegNo")
    }

    /** Returns true if the email id exists in database(checks is user Registered) */
     suspend fun isUserExists (email: String): Boolean {
        val  task  = usersRef.whereEqualTo("email", email).get()

        return if (task.isSuccessful) {
            !task.result.isEmpty
        } else {
            withContext(Dispatchers.Default){
                while (!task.isSuccessful) {
                    Log.d("TAG", "isUserExists: ${!task.isSuccessful}")
                    continue
                }
            }
            return !task.result.isEmpty
        }

    }

    /** Updates the [userInfo] to the current user */
    private fun getUserInfo() {
        val usrDocRef = usersRef.document(userRegNo.toString())

        fun setUsrInfo(docSnapshot: DocumentSnapshot){
            val user = docSnapshot.toObject(User::class.java)
            Log.d("BBB", "getUserInfo: $user")
            userInfo_!!.value = user
        }


           usrDocRef.get(Source.SERVER).addOnSuccessListener {
               if (it.exists()) {
                   setUsrInfo(it)
               } else {
                   usrDocRef.get(Source.SERVER).addOnSuccessListener { doc ->
                       setUsrInfo(doc)
                   }
               }
            }
    }


    /** Return the register number of the provided email id of the user
     * @param email Email of the user */
    suspend fun getRegisterNo(email: String): String? {
        var regNo: String? = null
        val task = usersRef.whereEqualTo("email", email).get()

        waitForTaskToComplete(task)

        for (document in task.result){
            regNo = document.get("regNo").toString()
        }
        Log.d("DOM", "RegisterNo: $regNo")
        return regNo
    }

    fun uploadProfileImg(url: Uri){
        val profileRef = mediaStorage.reference.child("Profile/${userInfo?.value?.regNo}.jpg")
        val uploadTask = profileRef.putFile(url)
        Log.d(TAG, "uploadProfileImg: $url")
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                usersRef.document(userInfo?.value?.regNo.toString())
                    .update("profileUrl", task.result)
            } else {
                // Handle failures
                // ...
            }
        }
    }

    val averageAttendance: MutableLiveData<String> = MutableLiveData()

    fun getAverageAttendance(){
        db.collection("attendance")
            .document(userInfo?.value?.regNo.toString()).get()
            .addOnSuccessListener {
                averageAttendance.value = it.get("average").toString()
            }
    }

    val pendingFees: MutableLiveData<Fees> = MutableLiveData()

    fun getPendingFees(){
        db.collection("fees")
            .document(userInfo?.value?.regNo.toString()).get()
            .addOnSuccessListener {
                pendingFees.value = it.toObject()
            }
    }

    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    *  >>                                                                                         >>
    *  >>                                       Fees                                              >>
    *  >>                                                                                         >>
    *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */


    suspend fun getFeesDetails(): Fees? {
        val task = db.collection("fees").document(userRegNo.toString()).get()

        waitForTaskToComplete<DocumentSnapshot>(task)

        val doc = task.result

        Log.d("FFF", "getFeesDetails: $doc")

        return if (doc.exists())
            doc.toObject(Fees::class.java)
        else
            null
    }




    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    *  >>                                                                                         >>
    *  >>                                     Attendance                                          >>
    *  >>                                                                                         >>
    *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */


    suspend fun getAttendance(): Attendance? {
        val task = db.collection("attendance").document(userRegNo.toString()).get()

        waitForTaskToComplete(task)

        val doc = task.result
        Log.d("ATTT", "getAttendance: ${doc.toObject(Attendance::class.java)}")
        return doc.toObject(Attendance::class.java)

    }


/* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
*  >>                                                                                         >>
*  >>                                    Notifications                                        >>
*  >>                                                                                         >>
*   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    private val clsName = userInfo?.value?.cls.toString()
    private val noteRef = db.collection("notifications")
        .document(clsName)
        .collection(clsName)

    private val _notificationList: MutableLiveData<List<NotificationObj>> = MutableLiveData()
    val notificationList: LiveData<List<NotificationObj>>
    get() = _notificationList


    fun getNotifications() {
        noteRef.limit(30)
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    val tempList = mutableListOf<NotificationObj>()
                    for (notify in value) {
                        tempList.add(notify.toObject<NotificationObj>())
                    }
                    _notificationList.value = tempList
                }
            }
    }


//    Get list of names of all classes
    val clsNamesList: MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    fun getClsNames(){
        Log.d("GGG", "getClsNames: Called")
        db.collection("classes").get().addOnSuccessListener {
            if (it != null){
                val tempList = mutableListOf<String>()
                for (doc in it){
                    tempList.add(doc.get("clsName").toString())
                }
                Log.d("GGG", "list: set done $tempList")
                clsNamesList.value = tempList
                Log.d("GGG", "live list: set done ${clsNamesList.value}")
            }
        }
    }


    suspend fun sendNotification(clsList: List<String>, notificationObj: NotificationObj){

        withContext(Dispatchers.Default){
            for (cls in clsList){
                db.collection("notifications")
                    .document(cls).collection(cls)
                    .document(notificationObj.time.toString())
                    .set(notificationObj)
//                waitForTaskToComplete(task)
                Log.d("EEE", "sendNotification: $cls sent")
            }
        }

        Log.d("SSS", "sendNotification: $clsList \n $notificationObj")
    }



    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    *  >>                                                                                         >>
    *  >>                                    Study Materials                                      >>
    *  >>                                                                                         >>
    *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    val subjectsList: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    private val storageRef = mediaStorage.reference.child("StudyMaterials")
    val chaptersList: MutableLiveData<List<StudyMaterialsPdf>> = MutableLiveData()
    private val subjectsRef = db.collection("materials").document("materials").collection(clsName)

    /** Gets the list of subjects */
    fun getSubjectsList(){
        Log.d("PPPP", "getSubjectsList: $clsName")
        subjectsRef
            .get()
            .addOnSuccessListener {
                val tempList = mutableListOf<String>()
                for (doc in it){
                    doc.getField<String>("subName")?.let { subName -> tempList.add(subName) }
                    Log.d("PPPP", "getSubjectsList: ${doc.getField<String>("subName")}")
                }
                subjectsList.value = tempList
                Log.d("PPPP", "getSubjectsList  live: ${subjectsList.value}")
            }
    }

    fun checkPresenceOfPdf(url: String): DownloadStatus{
        Log.d("ZZZZ", "ChapterList: $localChapterList")
        return if (localChapterList.contains(url))
            DownloadStatus.DOWNLOADED
        else
            DownloadStatus.NOT_DOWNLOADED
    }

    suspend fun downloadPdf(context: Context, pdf: StudyMaterialsPdf, subjectName: String): LiveData<DownloadStatus>{
        val status: MutableLiveData<DownloadStatus> = MutableLiveData(DownloadStatus.DOWNLOADING)
        Log.d("AAAAA", "downloadPdf: $pdf")
        withContext(Dispatchers.Default){
            val request = DownloadManager.Request(Uri.parse(pdf.pdfUrl))
            request.setTitle(pdf.chapterName)
                .setMimeType("application/pdf")
                .setAllowedOverMetered(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,
                    "UniKey/$subjectName/${pdf.chapterName}"
                )

            val dm = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        }

        return status
    }

    suspend fun getLocalFiles(subjectName: String){
        localChapterList = mutableListOf()
        withContext(Dispatchers.IO){

            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath + "/UniKey/$subjectName"

            val dir = File(path)
            if (dir.isDirectory){
                val list = dir.listFiles()
                Log.d("ZZZZ", "fakeDownload: dir >> $dir")
                if (list == null){
                    localChapterList.clear()
                    return@withContext
                }
                for(file in list){
                    localChapterList.add(file.name)
                    Log.d("ZZZZ", "fakeDownload: list >> ${file.name}")
                }
            } else {
                Log.d("ZZZZ", "fakeDownload: $dir is not directory")
            }
        }
    }

    fun getMaterialsList(subName: String){
        subjectsRef.document(subName).collection("chapters")
            .get().addOnSuccessListener {
                val temp = mutableListOf<StudyMaterialsPdf>()
                for (pdf in it){
                    temp.add(pdf.toObject())
                    Log.d("AAAA", "getMaterialsList: ${pdf.toObject<StudyMaterialsPdf>()}")
                }
                chaptersList.value = temp
            }

    }

    fun uploadPdf(clsName: String, subjectName: String, pdfName: String, pdfUri: Uri): LiveData<UploadStatus>{

        val status: MutableLiveData<UploadStatus> = MutableLiveData(UploadStatus.UPLOADING)

        val ref = storageRef.child("$subjectName/$pdfName.pdf")
        try{
            ref.putFile(pdfUri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("HHHH", "uploadPdf: ${it.result}")
                        val downloadUri = it.result
                        updatePdfData(
                            downloadUri = downloadUri,
                            clsName = clsName,
                            subjectName = subjectName,
                            pdfName = pdfName
                        ).observeForever { value ->
                            status.value = value
                        }
                    }
                }
        } catch (e: Exception){
            status.value = UploadStatus.FAILED
        }
        return status
    }

    private fun updatePdfData(downloadUri: Uri, clsName: String, subjectName: String, pdfName: String): LiveData<UploadStatus> {

        val status: MutableLiveData<UploadStatus> = MutableLiveData(UploadStatus.UPLOADING)
        val clsRef = db.collection("materials").document("materials").collection(clsName)
        val time = System.currentTimeMillis()

        clsRef.document(subjectName).apply {
            set(
                hashMapOf(Pair("subName",subjectName))
            )

            collection("chapters")
            .document(pdfName)
            .set(
                StudyMaterialsPdf(
                    time = time,
                    chapterName = pdfName,
                    pdfUrl = downloadUri.toString()
                ))
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d("HHHH", "uploadPdf: done")
                    status.value = UploadStatus.UPLOADED
                } else {
                    task.exception?.let {
                        throw it
                    }
                    status.value = UploadStatus.FAILED
                }
            }
        }

        return status
    }




    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
   *  >>                                                                                         >>
   *  >>                                    Gallery                                              >>
   *  >>                                                                                         >>
   *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    private val imageRef = mediaStorage.reference.child("GalleryImages")
    private val calendar = Calendar.getInstance()
    val imagesList: MutableLiveData<List<GalleryImage>> = MutableLiveData()

    fun getImages(){
        Log.d("LLLL", "getImages: ")
        imageRef.listAll().addOnSuccessListener { list->
            val images = list.items
            val tempList: MutableList<GalleryImage> = mutableListOf()

            images.forEach{ image->
                image.downloadUrl.addOnSuccessListener {
                    var url = ""
                    var date = ""
                    var time = ""
                    url = it.toString()

                    Log.d("LLLL", "getImages: Inside $url")

                    image.metadata.addOnSuccessListener { metaData ->
                        time = metaData.updatedTimeMillis.toString()
                        date = metaData.updatedTimeMillis.getDateFromMilliSec("dd-MM-yyyy")
                        tempList.add(
                            GalleryImage(
                                time = time,
                                date = date,
                                imgURL = url
                        )
                        )
                        Log.d("LLLL", "getImages: Inside $time $date $url")
                        imagesList.value = tempList
                    }
                }
            }
            Log.d("LLLL", "getImages: ${imagesList.value}  temp -> $tempList")
        }
    }

    private fun Long.getDateFromMilliSec(dateFormat: String): String{
        val formatter = SimpleDateFormat(dateFormat)
        calendar.timeInMillis = this
        return formatter.format(calendar.time)
    }


    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
   *  >>                                                                                         >>
   *  >>                                    Calendar Events                                           >>
   *  >>                                                                                         >>
   *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    private val eventRef = db.collection("Events")
    val eventsList: MutableLiveData<List<Event>> =  MutableLiveData()

    fun getEvents(){
        eventRef.get().addOnSuccessListener { result ->
            val tempList: MutableList<Event> = mutableListOf()
            for (doc in result){
                tempList.add(doc.toObject<Event>())
            }
            eventsList.value = tempList
            Log.d("OOOO", "getEvents: ${eventsList.value}")
        }
    }


    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
   *  >>                                                                                         >>
   *  >>                                    Certificate courses                                           >>
   *  >>                                                                                         >>
   *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    val coursesList: MutableLiveData<List<Course>> = MutableLiveData()

    fun getCoursesList(){
        db.collection("CertificateCourses")
            .get()
            .addOnSuccessListener {
                val tempList: MutableList<Course> = mutableListOf()
                for (doc in it){
                    tempList.add(doc.toObject<Course>())
                }
                coursesList.value = tempList
            }
    }


    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 *  >>                                                                                         >>
 *  >>                                    Job Alerts                                           >>
 *  >>                                                                                         >>
 *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    val jobsList: MutableLiveData<List<JobAlert>> = MutableLiveData()

    fun getJobsList(){
        db.collection("JobAlerts")
            .get()
            .addOnSuccessListener {
                val tempList: MutableList<JobAlert> = mutableListOf()
                for (doc in it){
                    tempList.add(doc.toObject<JobAlert>())
                }
                jobsList.value = tempList
            }
    }


    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 *  >>                                                                                         >>
 *  >>                                    About                                                 >>
 *  >>                                                                                         >>
 *   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */


    val about: MutableLiveData<About> = MutableLiveData()

    fun getAboutContent(){
        db.collection("About").document("About")
            .get()
            .addOnSuccessListener {
                about.value = it.toObject()
            }
    }


    //############################################################################################################
//############################################################################################################
    suspend fun putData(){
        withContext(Dispatchers.Default){
            db.collection("About").document("About")
                .set(About(
                    collegeName = "UniKey",
                    aboutContent = "Our mobile application ‘Vivek’ is an on the go easy to access college application for the students. With the usage of our applications students will now be aware of all the activities, events, attendance, exam results & many more. Now that the students have our application the students are totally upto-date with all college activities. The communication between the students  and the lectures is made easy with our application as lectures can share all the subject related queries and alerts via our application by which the lectures and students don’t need to share their personal social media details with each other hence an formal communication will happen."
                ))
                .addOnSuccessListener {
                    Log.d("PPPPP", "putData: Done")
                    getAboutContent()
                }
        }
    }

}





/** Makes the calling function to wait until the provided task is completed
* @param task Task to be completed */
private suspend fun <T> waitForTaskToComplete(task: Task<T>){
if (!task.isSuccessful){
    withContext(Dispatchers.Default){
        while (!task.isSuccessful) {
            Log.d("DOM", "Looping...")
            continue
        }
    }
}
}
