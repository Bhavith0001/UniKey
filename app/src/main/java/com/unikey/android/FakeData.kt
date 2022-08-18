package com.unikey.android

import com.unikey.android.objects.*

val notifyData = listOf(
    NotificationObj(
        title = "Title 1",
        description = "This is the description of the notification",
        content = "This is the content of the notification. You will get full explanation here",
        time = 1658921630829
    ),
    NotificationObj(
        title = "Title 2",
        description = "This is the description of the notification",
        content = "This is the content of the notification. You will get full explanation here",
        time = 1658921630830
    ),
    NotificationObj(
        title = "Title 3",
        description = "This is the description of the notification",
        content = "This is the content of the notification. You will get full explanation here",
        time = 1658921630831
    ),
    NotificationObj(
        title = "Title 4",
        description = "This is the description of the notification",
        content = "This is the content of the notification. You will get full explanation here",
        time = 1658921630832
    ),
    NotificationObj(
        title = "Title 5",
        description = "This is the description of the notification",
        content = "This is the content of the notification. You will get full explanation here",
        time = 1658921630833
    ),
    NotificationObj(
        title = "Title 6",
        description = "This is the description of the notification",
        content = "This is the content of the notification. You will get full explanation here",
        time = 1658921630834
    ),

)

//val eventsData = listOf(
//    Event(
//        id = 1,
//        title = "Keyboard breaker",
//        description = "this is speed typing event",
//        monthYear = "July 2022",
//        date = 12
//    ),
//    Event(
//        id = 2,
//        title = "Second event",
//        description = "this is second event",
//        monthYear = "July 2022",
//        date = 2
//    ),
//    Event(
//        id = 3,
//        title = "Third event",
//        description = "this is Third event",
//        monthYear = "June 2022",
//        date = 27
//    ),Event(
//        id = 4,
//        title = "Fourth breaker",
//        description = "this is Fourth typing event",
//        monthYear = "July 2022",
//        date = 15
//    ),
//    Event(
//        id = 5,
//        title = "Fifth event",
//        description = "this is Fifth event",
//        monthYear = "July 2022",
//        date = 30
//    ),
//    Event(
//        id = 6,
//        title = "Sixth event",
//        description = "this is Sixth event",
//        monthYear = "June 2022",
//        date = 13
//    ),
//    Event(
//        id = 7,
//        title = "Seventh event",
//        description = "this is Seventh event",
//        monthYear = "July 2022",
//        date = 17
//    )
//)

val datesOfEvent = listOf(
    "12 July 2022",
    "17 July 2022",
    "2 July 2022",
    "27 June 2022",
    "15 July 2022",
    "30 July 2022",
    "13 June 2022",
)


val subjectList = listOf(
    Subject("Network", "78"),
    Subject("E-Commerce", "79"),
    Subject("PFA", "80"),
    Subject("Android", "90"),
)

val chaptetLists = listOf(
    StudyMaterialsPdf(time = 1658921630830,chapterName = "Chapter2.pdf", pdfUrl = "https://fake/pah.pdf"),
    StudyMaterialsPdf(time = 1658921630831,chapterName = "Chapter3.pdf", pdfUrl = "https://fake/pth.pdf"),
    StudyMaterialsPdf(time = 1658921630832,chapterName = "Chapter4.pdf", pdfUrl = "https://fake/ath.pdf"),
    StudyMaterialsPdf(time = 1658921630833,chapterName = "Chapter5.pdf", pdfUrl = "https://fakepath.pdf"),
    StudyMaterialsPdf(time = 1658921630834,chapterName = "Chapter6.pdf", pdfUrl = "https://fak/path.pdf"),
    StudyMaterialsPdf(time = 1658921630835,chapterName = "Chapter7.pdf", pdfUrl = "https://fae/path.pdf"),
    StudyMaterialsPdf(time = 1658921630836,chapterName = "Chapter8.pdf", pdfUrl = "https://fke/path.pdf"),
    StudyMaterialsPdf(time = 1658921630837,chapterName = "Chapter9.pdf", pdfUrl = "https://ake/path.pdf"),
)

val fakeImageDataList = listOf<GalleryImage>(
    GalleryImage(
        time = "9487483595843",
        date = "01-08-2022",
        imgURL = "https://upload.wikimedia.org/wikipedia/commons/1/11/Android_Phone.jpg"
    ),
    GalleryImage(
        time = "9487483595847",
        date = "02-08-2022",
        imgURL = "https://upload.wikimedia.org/wikipedia/commons/1/11/Android_Phone.jpg"
    ),
    GalleryImage(
        time = "9487483595855",
        date = "03-08-2022",
        imgURL = "https://upload.wikimedia.org/wikipedia/commons/1/11/Android_Phone.jpg"
    ),

)

val fakeTimeTable = listOf(
    TimeTable(
        timeTableName = "1st Internal Exam",
        date = "04-08-2022",
        exam = listOf(
            Exam(time = "11:30am - 12:30pm", subName = "E-Commerce"),
            Exam(time = "01:30pm - 02:30pm", subName = "Network"),
        ),
        isMultiSub = true
    ),
    TimeTable(
        timeTableName = "1st Internal Exam",
        date = "05-08-2022",
        exam = listOf(
            Exam(time = "11:30am - 12:30pm", subName = "E-Commerce"),
            Exam(time = "01:30pm - 02:30pm", subName = "Network"),
        ),
        isMultiSub = true
    ),
    TimeTable(
        timeTableName = "1st Internal Exam",
        date = "06-08-2022",
        exam = listOf(
            Exam(time = "11:30am - 12:30pm", subName = "E-Commerce"),
            Exam(time = "01:30pm - 02:30pm", subName = "Network"),
        ),
        isMultiSub = true
    ),

)

val fakeTimeTableList = ExamTimeTableList(fakeTimeTable)

val fakeCourseList = listOf(
    Course(
        courseName = "PhotoShop",
        description = "This course teaches you in depth skills on photoshop",
        fees = "4000"
    ),
    Course(
        courseName = "Android development",
        description = "This course teaches you in depth skills on Android development",
        fees = "5000"
    ),
    Course(
        courseName = "Java",
        description = "This course teaches you in depth skills on Java",
        fees = "2000"
    ),

)

val fakeJobs = listOf(
    JobAlert(
        companyName = "Google",
        jobRole = "Android Developer",
        jobDescription = "As an android developer",
        salaryPackage = "5 LPA",
        link = "https://www.google.com"
    ),
    JobAlert(
        companyName = "Amazon",
        jobRole = "Web Developer",
        jobDescription = "As an web developer",
        salaryPackage = "5 LPA",
        link = "https://www.google.com"
    ),
    JobAlert(
        companyName = "Netflix",
        jobRole = "Data Analyst",
        jobDescription = "As an data analyst",
        salaryPackage = "5 LPA",
        link = "https://www.google.com"
    ),

)