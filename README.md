# SkinDiseaseClassificationApp
The development of an Android application that can identify skin diseases using deep learning.  
The User opens the application to a login screen which requires his email, username, and password. Then according to the registered type of this user whether he’s a patient or a doctor the application will navigate the user to a different view of the app. If the user hasn’t registered yet he can click on the sign-up button which navigates him to the registration screen. This screen asks the user to add a photo, enter his first and last name, gender, type, email, password and to confirm the entered password. Then clicking on the sign-up button returns him to the login screen where he can login to start using the app.  
First Scenario: The User is a Patient!  
![1](https://github.com/NadaMuhammed/SkinDiseaseClassificationApp/assets/93039383/e44861ff-800c-4e67-ad62-400ac50d77ad)
![2](https://github.com/NadaMuhammed/SkinDiseaseClassificationApp/assets/93039383/3ca1aa7c-c922-4b41-8c7f-1612c8c80790)

Bottom navigation view of 4 screens.  
The first screen is the home screen where the user can search for a disease by its name in a search bar or he can read from a shown list of different diseases. Clicking on a disease will navigate him to a new page of this disease showing a picture, name, description, and treatment of this disease.  
The second screen is the scan screen. It shows an instructions dialog of how to take a clear photo, dismissing this dialog shows two buttons for uploading a photo for diagnosing: One for browsing from the gallery and the other opens the camera. Once the user uploads the photo and clicks the scan button the application will use the attached machine model to predict the disease and shows the result to the user.  
The third screen is the doctors’ screen which is called “Ask” screen. It shows a list of doctors which the user can chat with. Choosing a doctor will navigate the user to a chat screen between him and this doctor.  
The fourth and the last screen is the settings or profile screen. This screen shows the details of the logged in user which he entered while registering such as: his photo, username, first and last name, email, and type. And a logout button to logout from the application.  

Second Scenario: The User is a Doctor! 
![As a Doctor!](https://github.com/NadaMuhammed/SkinDiseaseClassificationApp/assets/93039383/46b8c3d8-60a4-48ee-a77e-fb4b4b5f6afd)

Bottom navigation view of 2 screens.  
The first screen is the chat screen. This screen shows to the doctor the patients’ chats to respond to their questions and help them. Clicking on a patient’s chat also navigates the doctor to a chat screen between the doctor and the patient showing the sent messages.  
The second and the last screen is the settings or profile screen same as the patients’ screen showing the photo, username, first and last name, email, type of the doctor, And a logout button. 

This project is charecterized with privacy as an encryption and decryption algorithm was used for messages which is AES method. The message string is first converted into Byte, then encrypted using AES method and then encrypted byte is converted back to String using a standard character set. This encrypted byte string is then stored in the Firebase. So, as the data stored in the Firebase database is in encrypted form, even if anyone is able to get the access to the database, the actual message will not be readable by the person until s/he has the encryption method and key available with him. Similarly, on the decryption side, the received string from the database is converted back to byte using the same character set (used on the encryption side). Once the encrypted byte data is available, then the decrypt mode cipher is used to decrypt the data. 


Used Mvvm, Room, Coroutines, tensorflowlite, firebase, navigation components, recycler view and android material designs for UI.
