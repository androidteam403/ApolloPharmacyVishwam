package com.apollopharmacy.vishwam.data.model.attendance

data class TaskInfoReq(
    var TASKNAME: String,
    var EMPID: String,
    var TASKID: String,
    var LATITUDE: String,
    var LONGITUDE: String,
    var TYPE: String,
    var CITY: String
)