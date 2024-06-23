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

![Face Haven't Register](path/to/face_havent_register.png)
![Input Name and Matric for Register](path/to/input_name_and_matric.png)

This feature allows users to input their name and matric number to register their face seamlessly. The registration process is quick and efficient, taking approximately five seconds after the user inputs their details. The system ensures that only valid face data is captured and provides immediate feedback to the user upon successful registration.

#### Attendance Fast and Efficient

![Attendance Mode](path/to/attendance_mode.png)
![Indicator Face Have Registered](path/to/indicator_face_have_registered.png)

When the attendance mode is activated, the system detects previously registered faces with high accuracy and speed (900 milliseconds). It uses L2Norm and cosine similarity to ensure robust face recognition performance across various lighting conditions. The results of accuracy tests using rear and front cameras of a Samsung S22 are provided to highlight the system's performance.

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

![Registered List](path/to/registered_list.png)
![Attend Class](path/to/attend_class.png)

The Registered List tab displays all registered faces, and the Attendance Class tab shows the order of students who have attended, with indicators for clock-in and clock-out times. These tabs provide detailed information and functionalities for managing attendance records.

#### Summarize

![Summarize](path/to/summarize.png)
![Pick Date on Summarize](path/to/pick_date_on_summarize.png)

The Summarize feature allows users to quickly overview all attendance records and select specific dates to display attendance details.

#### Export to Excel Feature

![Excel Button](path/to/excel_button.png)
![Download Folder](path/to/download_folder.png)
![Indication Successful Export](path/to/indication_successful_export.png)
![Result of Export to Excel](path/to/result_of_export_to_excel.png)

This feature enables users to export attendance data to an Excel file, which is saved in the "Download" folder with a unique name including the date and time of export. The exported file contains comprehensive attendance records.

#### Data Persistent

![Attendance Entity](path/to/attendance_entity.png)
![Students Entity](path/to/students_entity.png)

The project uses the Room library for data persistence, supporting CRUD operations. Two tables, Students Entity and Attendance Entity, are used to manage student and attendance records efficiently.

### Minimum Specification

The application was tested on various devices, ensuring robust compatibility and performance across flagship, mid-range, and entry-level phones. A minimum SDK of 27 (Android Oreo) is required, and the quality of the phone's camera significantly affects the application's performance.

![Minimum Specification](path/to/minimum_specification.png)
