# Android-Based Face Recognition System for Class Attendance

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/a177e642-6f4a-4e5c-a822-723546141d5b)


### Introduction

This chapter delineates the preliminary findings derived from the development and testing phases of the Android-based face recognition system designed for class attendance. The chapter encompasses a comprehensive analysis of the performance metrics of the machine learning model, the architectural design of the user interface, and the overall operational functionality of the application. The insights garnered from these initial results are pivotal in assessing the feasibility and effectiveness of the proposed system.

### Machine Learning Model

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/84a09014-0cf2-4f6c-ad47-2716484c1f1a)


For this project, one of the scopes that have been achieved is to create a model that can detect and identify individuals in a picture. This demonstrates the feasibility of completing and further developing this project.

The model used in this project employs transfer learning, utilizing a pre-trained model developed by Google. This approach significantly reduces the development time while maintaining high accuracy, reaching up to 95%. The model's directory structure includes known and unknown folders, used for registering faces and recognizing attendance, respectively.

### Android Application

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/2f8a4502-4a65-4b2f-9c6c-db4ed72072b0)

The development of the Android application integrates face recognition technology to streamline attendance management. The key features of this application are designed to enhance efficiency, accuracy, and user experience.

#### One-Time Register Face

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/e70b3c03-0fb4-41f4-954d-7d5432ed22ca)

One of the standout features of this application is the one-time face registration capability, which allows users to input their name and matric number to register their face seamlessly that shown on figure 18. The registration process is designed to be quick and efficient, taking approximately five seconds to complete after the user inputs their details.

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/86d8ca58-1389-4ce1-a5f5-cd716c421ec2)

Face recognition is a key component of this application, leveraging advanced machine learning techniques. The registration process is initiated only when a face is detected by the camera. If no face is detected, the system will reject the registration attempt and prompt the user to retry, ensuring that only valid face data is captured.
To indicate successful registration, the application displays a notification on the screen confirming that the face has been registered. This feature ensures a smooth and reliable user experience, providing immediate feedback and minimizing the need for repeated attempts.

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/7440a2a9-f7a3-4454-8a4c-f65f8d7f040c)



#### Attendance Fast and Efficient

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/06fe25df-61e7-4a21-b48f-5684082e025f)

The second feature on this application is when the slider for attendance mode is pressed, the attendance mode will be activated. A pop-up will appear, displaying the detected face, and offering three menu options. The first menu option allows you to save the attendance by clocking in, indicated by a yellow button. The second option is 
When the attendance mode is activated, the system detects previously registered faces with high accuracy and speed (900 milliseconds). It uses L2Norm and cosine similarity to ensure robust face recognition performance across various lighting conditions. The results of accuracy tests using rear and front cameras of a Samsung S22 are provided to highlight the system's performance.

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/15028fbb-3246-41cc-a14e-946ed846cd32)

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/93c732c4-ac01-41d3-85b0-c1fd300f4440)



##### Accuracy Results

| Device           | Camera         | Accuracy   |
|------------------|----------------|------------|
| Samsung S22      | Rear Camera    | 95%        |
| Samsung S22      | Front Camera   | 93%        |

##### Processing Time

| Operation        | Time (ms)      |
|------------------|----------------|
| Registration     | 5000           |
| Face Detection   | 900            |

#### Attend Class and Registered List

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/6388f6e2-14af-4bd2-aacd-564c74bbe82f)

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/643a8ec0-6832-429e-91af-8fc1aa170f20)


The Registered List tab displays all registered faces, and the Attendance Class tab shows the order of students who have attended, with indicators for clock-in and clock-out times. These tabs provide detailed information and functionalities for managing attendance records.

#### Summarize

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/8341c596-e8d0-42be-a172-4b10549a806a)

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/4334e47e-3f63-4956-8e43-4afa9f18e27f)


The Summarize feature allows users to quickly overview all attendance records and select specific dates to display attendance details.

#### Export to Excel Feature

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/361db0a4-ad85-4f02-84c7-d0dc115a22fb)
This feature is available on the Registered List tab layout, indicated by a button with an Excel logo. To use this feature, simply press the Excel logo, and the application will process the data for export to an Excel file. 
![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/5748140a-ee7e-4576-ba6e-7acf4bcbeff4)
The exported Excel file can be found in the "Download" folder of your phone's file manager application. The file name will include the date and time of export to prevent any naming conflicts within the application and on the device. This naming convention ensures that each exported file is unique and easily identifiable.
![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/c6856e01-c65e-41c5-ade4-465353cf8001)
Upon completion of the export process, a pop-up notification will appear on your phone indicating that the export has finished. The Excel file will contain detailed information, including the name of the student, date, time, and type of attendance. The "type" field specifies whether the entry is for clock-in or clock-out, with "IN" representing clock-in and "OUT" representing clock-out. This feature provides a comprehensive and organized record of attendance data.
![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/be7be434-c4e9-4e49-97a9-ca5c99c15e70)


This feature enables users to export attendance data to an Excel file, which is saved in the "Download" folder with a unique name including the date and time of export. The exported file contains comprehensive attendance records.

#### Data Persistent

This project utilizes the Room library as the primary method for data persistence. Room was chosen for its powerful data management capabilities and ease of access. As Room supports the CRUD (Create, Read, Update, Delete) operations, it provides a robust framework for managing application data efficiently.
![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/a903b0bc-41ea-45e1-b658-8097a7563d36)
In this project, two tables are used: the Students Entity and the Attendance Entity. The structure of these tables is illustrated in Figure 29 and detailed in the appendix. The Students Entity table includes fields such as student name, matriculation number, date, and image URI. On the other hand, the Attendance Entity table includes fields such as student matriculation number, attendance date, and type (indicating whether the record is for clock-in or clock-out).
![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/49348cbc-07c2-4e03-a5b6-abeee981e9a7)
These tables are connected using a join operation on the matriculation number, ensuring that each student's records are accurately linked. The matriculation number serves as a unique identifier, preventing any conflicts and ensuring data integrity. The implementation details of the join operation and table structures can be found in the appendix. By leveraging the Room library, the project ensures efficient data handling, making the application reliable and easy to maintain.

### Minimum Specification

The application was tested on various devices, ensuring robust compatibility and performance across flagship, mid-range, and entry-level phones. A minimum SDK of 27 (Android Oreo) is required, and the quality of the phone's camera significantly affects the application's performance.

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/c7c40357-6c9d-443a-84fd-4bb2684ca512)
![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/61130d8a-c204-4788-9580-4cebb74582f6)
![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/2f8494e3-111c-497b-a868-ec26959fa15b)



