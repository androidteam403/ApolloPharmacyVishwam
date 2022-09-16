package com.apollopharmacy.vishwam.ui.home;

import com.apollopharmacy.vishwam.data.Preferences;
import com.apollopharmacy.vishwam.data.model.ValidateResponse;
import com.google.gson.Gson;

public class MainActivityController {
    MainActivityCallback mainActivityCallback;

    public MainActivityController(MainActivityCallback mainActivityCallback) {
    }

//   public void getRole(String validatedEmpId) {
//        String url = Preferences.INSTANCE.getApi();
//       String data = Gson.fromJson(url, ValidateResponse::class.java)
////        for (i in data.APIS.indices) {
////            if (data.APIS[i].NAME.equals("CMS TICKETLIST")) {
////                val baseUrl = data.APIS[i].URL
//        // "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/list/reason-list?page=1&rows=100"
//        //val token = data.APIS[i].TOKEN
////
////                val new = if (status.contains("new")) "new" else ""
////                val inprogress = if (status.contains("inprogress")) "inprogress" else ""
////                val solved = if (status.contains("solved")) "solved" else ""
////                val rejected = if (status.contains("rejected")) "rejected" else ""
////                val reopened = if (status.contains("reopened")) "reopened" else ""
////                val closed = if (status.contains("closed")) "closed" else ""
//
//        val baseUrl: String =
//                "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/user/select/employee-details-mobile?emp_id=${validatedEmpId}"
//
////"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/mobile-ticket-list-by-emp-id?&employee_id=${requestComplainList.empid}&status=${status}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}&page=${requestComplainList.page}&rows=10"
//        viewModelScope.launch {
//            state.value = State.SUCCESS
//            val response = withContext(Dispatchers.IO) {
//                RegistrationRepo.getDetails(
//                        "h72genrSSNFivOi/cfiX3A==",
//                        GetDetailsRequest(
//                                baseUrl,
//                                "GET",
//                                "The",
//                                "",
//                                ""
//                        )
//                )
//            }
//            when (response) {
//                is ApiResult.Success -> {
//                    state.value = State.ERROR
//                    if (response != null) {
//                        val resp: String = response.value.string()
//                        if (resp != null) {
//                            val res = BackShlash.removeBackSlashes(resp)
//                            val responseNewTicketlist =
//                                    Gson().fromJson(
//                                            BackShlash.removeSubString(res),
//                                            EmployeeDetailsResponse::class.java
//                                )
//                            if (responseNewTicketlist.success!!) {
//                                employeeDetails.value = responseNewTicketlist
////                                        newcomplainLiveData.value =
////                                            responseNewTicketlist.data.listData.rows
//                            }
//                            else {
//                                command.value =
//                                        CmsCommand.ShowToast(responseNewTicketlist.message.toString())
//                            }
//                        }
//                        //  unComment it  newcomplainLiveData.value = response.value.data.listData.rows
//                        //  Ticketlistdata = response.value
//                        //  val reasonlitrows = response.value.data.listData.rows
//                        // for (row in reasonlitrows) {
//                        //  deartmentlist.add(row.department)
//                        // }
//                    } else {
//                        //  unComment it   command.value = CmsCommand.ShowToast(response.value.message.toString())
//                    }
//                }
//                is ApiResult.GenericError -> {
//                    state.value = State.ERROR
//                }
//                is ApiResult.NetworkError -> {
//                    state.value = State.ERROR
//                }
//                is ApiResult.UnknownError -> {
//                    state.value = State.ERROR
//                }
//                is ApiResult.UnknownHostException -> {
//                    state.value = State.ERROR
//                }
//            }
//        }
////            }
////        }
//    }
}
