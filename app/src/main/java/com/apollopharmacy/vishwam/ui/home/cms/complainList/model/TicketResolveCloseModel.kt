package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class TicketResolveCloseModel(
    val action: String,
    val comment: String,
    val employee_id: String,
    val status: String,
    val ticket_id: String,
    val feedback : Feedback?

)
data class Feedback(
    val rating: Rating?
)
data class Rating(
    val uid: String
)