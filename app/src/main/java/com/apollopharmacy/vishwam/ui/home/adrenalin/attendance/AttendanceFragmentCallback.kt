package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance

interface AttendanceFragmentCallback {
    fun onSuccessAttendanceSignedOut(message: String)
    fun onSuccessAttendanceSignedIn(message: String)
    fun onItemClick(position: Int, imagePath: String, name: String)
    fun deleteImage(position: Int)

}
//Toast.makeText(requireContext(), "Attendance Signed-Out Succesfully", Toast.LENGTH_SHORT).show()
