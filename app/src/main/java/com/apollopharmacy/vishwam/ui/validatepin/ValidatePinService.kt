package com.apollopharmacy.vishwam.ui.validatepin

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ValidatePinService : Service() {
    private val client = OkHttpClient()
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Extract necessary data from the intent
        val validatedEmpId = intent?.getStringExtra("validatedEmpId") ?: ""
        getRole(validatedEmpId)
        return START_NOT_STICKY
    }

    private fun getRole(validatedEmpId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = Preferences.getApi()
                val data = Gson().fromJson(url, ValidateResponse::class.java)

                var proxyUrl = ""
                var proxyToken = ""
                for (i in data.APIS.indices) {
                    if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                        proxyUrl = data.APIS[i].URL
                        proxyToken = data.APIS[i].TOKEN
                        break
                    }
                }

                var baseUrl = ""
                var token = ""
                for (i in data.APIS.indices) {
                    if (data.APIS[i].NAME.equals("SWND employee-details-mobile")) {
                        baseUrl = data.APIS[i].URL
                        token = data.APIS[i].TOKEN
                        break
                    }
                }
                val response = withContext(Dispatchers.IO) {
                    RegistrationRepo.getDetails(
                        proxyUrl, proxyToken,
                        GetDetailsRequest(baseUrl + "?emp_id=$validatedEmpId", "GET", "The", "", "")
                    )
                }
                when (response) {
                    is ApiResult.Success -> {
                        if (response != null) {
                            val resp: String = response.value.string()
                            if (resp != null) {
//                                Handler(Looper.getMainLooper()).postDelayed({
                                    //Do something after 100ms
                                    try {
                                        val res = BackShlash.removeBackSlashes(resp)

                                        val it =
                                            Gson().fromJson(
                                                BackShlash.removeSubString(res),
                                                EmployeeDetailsResponse::class.java
                                            )
                                        if (it.success!!) {
                                            if (it.success!!) {
                                                Preferences.setEmployeeApiAvailable(true);
                                                if (it.success!! && it.data != null && it.data?.uploadSwach != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                       it.data!!.uploadSwach!!.uid = "Yes"

//                        it.data!!.role!!.code="region_head"
                                                    Preferences.storeEmployeeDetailsResponseJson(Gson().toJson(it))
                                                    Preferences.setRoleForCeoDashboard(it.data!!.role!!.code.toString())
//                       Toast.makeText(applicationContext, ""+ Preferences.getRoleForCeoDashboard(), Toast.LENGTH_SHORT).show()
                                                    if (it.data?.uploadSwach?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                                        Preferences.setEmployeeRoleUid(it.data?.uploadSwach?.uid!!)
                                                        if (it.data?.uploadSwach?.uid!!.equals(
                                                                "Yes", true
                                                            )
                                                        ) {
                                                            if (it.data?.swacchDefaultSite != null && it.data?.swacchDefaultSite?.site != null) {
                                                                Preferences.setSwachhSiteId(it.data?.swacchDefaultSite?.site!!)
                                                            } else {
                                                                Preferences.setSwachhSiteId("")
                                                            }
                                                        }

                                                    } else {
                                                        Preferences.setEmployeeRoleUid("No")
                                                    }
                                                } else {
                                                    Preferences.setEmployeeRoleUid("No")
                                                }

                                                if (it.data != null && it.data?.uploadApnaRetroQr != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                                                    Preferences.storeEmployeeDetailsResponseJsonRetroQr(Gson().toJson(it))
                                                    if (it.data?.uploadApnaRetroQr?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                                        Preferences.setEmployeeRoleUidRetroQr(it.data?.uploadApnaRetroQr?.uid!!)
                                                        if (it.data?.uploadApnaRetroQr?.uid!!.equals(
                                                                "Yes", true
                                                            )
                                                        ) {
                                                            Preferences.setRetroQrEmployeeRoleUid(it.data?.uploadApnaRetroQr?.uid!!)
                                                        } else {
                                                            Preferences.setRetroQrEmployeeRoleUid("")
                                                        }

                                                    } else {
                                                        Preferences.setRetroQrEmployeeRoleUid("")
                                                    }
                                                } else {
                                                    Preferences.setRetroQrEmployeeRoleUid("")
                                                }

                                                if (it.data != null && it.data?.uploadApnaRetro != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                                                    Preferences.storeEmployeeDetailsResponseJsonNewDrug(Gson().toJson(it))
                                                    if (it.data?.uploadApnaRetro?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                                        Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.uploadApnaRetro?.uid!!)
                                                        if (it.data?.uploadApnaRetro?.uid!!.equals(
                                                                "Yes", true
                                                            )
                                                        ) {
                                                            Preferences.setRetroEmployeeRoleUid(it.data?.uploadApnaRetro?.uid!!)
                                                        } else {
                                                            Preferences.setRetroEmployeeRoleUid("")
                                                        }

                                                    } else {
                                                        Preferences.setRetroEmployeeRoleUid("")
                                                    }
                                                } else {
                                                    Preferences.setRetroEmployeeRoleUid("")
                                                }

                                                if (it.data != null && it.data?.champs_admin != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
//                        Preferences.storeEmployeeDetailsResponseJsonChampsAdmin(Gson().toJson(it))
                                                    if (it.data?.champs_admin?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                                        if (it.data?.champs_admin?.uid != null && it.data?.champs_admin?.uid!!.equals(
                                                                "Yes", true
                                                            )
                                                        ) {
                                                            Preferences.setEmployeeRoleUidChampsAdmin(it.data?.champs_admin?.uid!!)
                                                        } else {
                                                            Preferences.setEmployeeRoleUidChampsAdmin("")
                                                        }

                                                    } else {
                                                        Preferences.setEmployeeRoleUidChampsAdmin("")
                                                    }
                                                } else {
                                                    Preferences.setEmployeeRoleUidChampsAdmin("")
                                                }

                                                if (it.data != null && it.data?.newDrugRequest != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                                                    Preferences.storeEmployeeDetailsResponseJsonNewDrug(Gson().toJson(it))
                                                    if (it.data?.newDrugRequest?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                                        Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.newDrugRequest?.uid!!)
                                                        if (it.data?.newDrugRequest?.uid!!.equals(
                                                                "Yes", true
                                                            )
                                                        ) {
                                                            Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.newDrugRequest?.uid!!)
                                                        } else {
                                                            Preferences.setEmployeeRoleUidNewDrugRequest("")
                                                        }

                                                    } else {
                                                        Preferences.setEmployeeRoleUidNewDrugRequest("")
                                                    }
                                                } else {
                                                    Preferences.setEmployeeRoleUidNewDrugRequest("")
                                                }


                                            } else {
                                                Preferences.setEmployeeApiAvailable(false);
                                            }
//                                            broadcastResult(responseNewTicketlist)
                                            if (MainActivity.isMainActivityRunning) {
                                                val refreshIntent = Intent("ACTION_REFRESH_HOME_FRAGMENT")
                                                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(refreshIntent)
                                                stopSelf()
                                            }
                                        } else {
//
                                        }
                                    } catch (e: Exception) {
                                        Log.e("API Error", "Received HTML response")

                                    }
//                                }, 60000)

                            }

                        } else {

                        }
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                Log.e("API Service", "Error calling API", e)
            }
        }
    }

    // Method to broadcast result back to activity or handle it as needed
    private fun broadcastResult(data: EmployeeDetailsResponse) {
        val intent = Intent("com.apollopharmacy.vishwam.ACTION_RECEIVE_DATA") // Custom action to avoid conflicts
        intent.putExtra("data", Gson().toJson(data))
        sendBroadcast(intent)
    }
}
