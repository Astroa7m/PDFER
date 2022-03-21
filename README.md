# PDFER
App demo that uses WorkManager API to download and schedule PDFs in the background

# Download PDF
You can either download custom pdf files by placing your preferable pdf file url in the text field or download predefined url by clicking on test.

<p align="center">
  <img src="https://user-images.githubusercontent.com/90382113/159090412-4975e1e9-d218-4681-a791-f67fbe6ebb99.png" width="270" height="480">
</p>


# Download Process
Starting | Running | Finished
:-------------------------:|:------------:|:-----------:
<img src="https://user-images.githubusercontent.com/90382113/159091483-191af9e9-d103-45cb-947b-70034a287901.png" width="270" height="480"> | <img src="https://user-images.githubusercontent.com/90382113/159091517-fcebe738-beba-4269-a65f-11dcc1339e0d.png" width="270" height="480"> | <img src="https://user-images.githubusercontent.com/90382113/159091556-be0e630c-4aa5-456e-94be-28884e88e4b5.png" width="270" height="480">




# Post-Download
You can inspect your downloaded files from the menu item in the home screen .
Inspect | View | Share
:-------------------------:|:------------:|:-----------:
<img src="https://user-images.githubusercontent.com/90382113/159091699-85463a0f-56de-4a1a-9994-4ed5d0e4e80c.png" width="270" height="480"> | <img src="https://user-images.githubusercontent.com/90382113/159091715-7e688e0c-177a-4c01-a7ff-dcf7208827e3.png" width="270" height="480"> | <img src="https://user-images.githubusercontent.com/90382113/159091735-68eb8950-32e8-445f-8b4a-8851761d6c4f.png" width="270" height="480">

# Key notes:
* Downloading requires internet connection, if any download request happens when offline, then WorkManager will schedule them to run when any connection is found even when the app is dead.
* By cancelling the download from the notification label; you first cancel the background work, and it requires you again to cancel the notification by clicking once more.
