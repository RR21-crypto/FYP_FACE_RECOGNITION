# Android-Based Face Recognition System for Class Attendance

![Banner](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/a177e642-6f4a-4e5c-a822-723546141d5b)

## Introduction

This chapter delineates the preliminary findings derived from the development and testing phases of the Android-based face recognition system designed for class attendance. The chapter encompasses a comprehensive analysis of the performance metrics of the machine learning model, the architectural design of the user interface, and the overall operational functionality of the application. The insights garnered from these initial results are pivotal in assessing the feasibility and effectiveness of the proposed system.

## Machine Learning Model

![Model Performance](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/84a09014-0cf2-4f6c-ad47-2716484c1f1a)

For this project, one of the scopes that have been achieved is to create a model that can detect and identify individuals in a picture. This demonstrates the feasibility of completing and further developing this project.

The model used in this project employs transfer learning, utilizing a pre-trained model developed by Google. This approach significantly reduces the development time while maintaining high accuracy, reaching up to 95%. The model's directory structure includes known and unknown folders, used for registering faces and recognizing attendance, respectively.

## Android Application

![Application Interface](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/2f8a4502-4a65-4b2f-9c6c-db4ed72072b0)

The development of the Android application integrates face recognition technology to streamline attendance management. The key features of this application are designed to enhance efficiency, accuracy, and user experience.

### One-Time Register Face

![Face Registration](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/e70b3c03-0fb4-41f4-954d-7d5432ed22ca)

One of the standout features of this application is the one-time face registration capability, which allows users to input their name and matric number to register their face seamlessly.

![Registration Process](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/86d8ca58-1389-4ce1-a5f5-cd716c421ec2)

Face recognition is a key component of this application, leveraging advanced machine learning techniques. The registration process is initiated only when a face is detected by the camera. If no face is detected, the system will reject the registration attempt and prompt the user to retry, ensuring that only valid face data is captured.

![Successful Registration](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/7440a2a9-f7a3-4454-8a4c-f65f8d7f040c)

### Attendance Fast and Efficient

![Attendance Mode](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/06fe25df-61e7-4a21-b48f-5684082e025f)

When the attendance mode is activated, the system detects previously registered faces with high accuracy and speed (900 milliseconds). It uses L2Norm and cosine similarity to ensure robust face recognition performance across various lighting conditions.

![Attendance Options](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/15028fbb-3246-41cc-a14e-946ed846cd32)

![Detected Face](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/93c732c4-ac01-41d3-85b0-c1fd300f4440)

#### Accuracy Results

| Device       | Camera      | Accuracy |
|--------------|-------------|----------|
| Samsung S22  | Rear Camera | 95%      |
| Samsung S22  | Front Camera| 93%      |

#### Processing Time

| Operation     | Time (ms) |
|---------------|-----------|
| Registration  | 5000      |
| Face Detection| 900       |

### Attend Class and Registered List

![Registered List](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/6388f6e2-14af-4bd2-aacd-564c74bbe82f)

The Registered List tab displays all registered faces, and the Attendance Class tab shows the order of students who have attended, with indicators for clock-in and clock-out times. These tabs provide detailed information and functionalities for managing attendance records.

### Summarize

![Summarize](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/8341c596-e8d0-42be-a172-4b10549a806a)

The Summarize feature allows users to quickly overview all attendance records and select specific dates to display attendance details.

### Export to Excel Feature

![Excel Button](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/361db0a4-ad85-4f02-84c7-d0dc115a22fb)

This feature is available on the Registered List tab layout, indicated by a button with an Excel logo. To use this feature, simply press the Excel logo, and the application will process the data for export to an Excel file.

![Download Folder](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/5748140a-ee7e-4576-ba6e-7acf4bcbeff4)

The exported Excel file can be found in the "Download" folder of your phone's file manager application. The file name will include the date and time of export to prevent any naming conflicts within the application and on the device.

![Export Successful](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/c6856e01-c65e-41c5-ade4-465353cf8001)

Upon completion of the export process, a pop-up notification will appear on your phone indicating that the export has finished. The Excel file will contain detailed information, including the name of the student, date, time, and type of attendance.

![Excel File](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/be7be434-c4e9-4e49-97a9-ca5c99c15e70)

### Data Persistence

This project utilizes the Room library as the primary method for data persistence. Room supports CRUD (Create, Read, Update, Delete) operations, providing a robust framework for managing application data efficiently.

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/4e1e3007-de7a-4480-8e69-f6768fee1d69)


In this project, two tables are used: the Students Entity and the Attendance Entity.

![image](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/f7c8f46d-5721-41c6-a354-77323cd8f860)


These tables are connected using a join operation on the matriculation number, ensuring that each student's records are accurately linked.

### Minimum Specification

The application was tested on various devices, ensuring robust compatibility and performance across flagship, mid-range, and entry-level phones. A minimum SDK of 27 (Android Oreo) is required, and the quality of the phone's camera significantly affects the application's performance.

![Specifications](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/c7c40357-6c9d-443a-84fd-4bb2684ca512)

![Specifications](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/61130d8a-c204-4788-9580-4cebb74582f6)

![Specifications](https://github.com/RR21-crypto/FYP_FACE_RECOGNITION/assets/81364035/2f8494e3-111c-497b-a868-ec26959fa15b)
