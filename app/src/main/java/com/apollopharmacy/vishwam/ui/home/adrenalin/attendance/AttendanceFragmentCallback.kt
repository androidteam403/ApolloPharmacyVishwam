package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance

interface AttendanceFragmentCallback {
    fun onSuccessAttendanceSignedOut(message: String)
    fun onSuccessAttendanceSignedIn(message: String)

}
//Toast.makeText(requireContext(), "Attendance Signed-Out Succesfully", Toast.LENGTH_SHORT).show()
