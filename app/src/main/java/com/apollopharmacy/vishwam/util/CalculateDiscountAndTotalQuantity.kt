package com.apollopharmacy.vishwam.util

import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.data.model.discount.RejectedOrderResponse


object CalculateDiscountAndTotalQuantity {
    fun calculateTotalCost(pendingOrder: List<PendingOrder.ITEMSItem>): Double {
        var pendingOrderdata = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            pendingOrderdata.add(Pendingdata)
        }
        return pendingOrderdata.sumByDouble { it }
    }


    fun CalculateTotalDiscount(pendingOrder: List<PendingOrder.ITEMSItem>): Double {
        var pendingOrderdata = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            var percentagCal = (Pendingdata * pendingOrder[i].APPROVED_DISC!!) / 100
            pendingOrderdata.add(percentagCal)
        }
        return pendingOrderdata.sumByDouble { it }
    }

    fun CalculateNetPayment(pendingOrder: List<PendingOrder.ITEMSItem>): Double {
        var totalArrayList = ArrayList<Double>()
        var discountTotalArrayList = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            var discountTotal = (Pendingdata * pendingOrder[i].APPROVED_DISC!!) / 100
            totalArrayList.add(Pendingdata)
            discountTotalArrayList.add(discountTotal)
        }
        var totalAdd = totalArrayList.sumByDouble { it }
        var totalDiscount = discountTotalArrayList.sumByDouble { it }
        return totalAdd - totalDiscount

    }

    fun calculateTotalCostForApproval(pendingOrder: List<ApprovalOrderRequest.ITEMSItem>): Double {
        var pendingOrderdata = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            pendingOrderdata.add(Pendingdata)
        }
        return pendingOrderdata.sumByDouble { it }
    }

    fun CalculateTotalDiscountForApproval(pendingOrder: List<ApprovalOrderRequest.ITEMSItem>): Double {
        var pendingOrderdata = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            var percentagCal = (Pendingdata * pendingOrder[i].APPROVED_DISC!!) / 100
            pendingOrderdata.add(percentagCal)
        }
        return pendingOrderdata.sumByDouble { it }
    }


    fun CalculateNetPaymentForApproval(pendingOrder: List<ApprovalOrderRequest.ITEMSItem>): Double {
        var totalArrayList = ArrayList<Double>()
        var discountTotalArrayList = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            var discountTotal = (Pendingdata * pendingOrder[i].APPROVED_DISC!!) / 100
            totalArrayList.add(Pendingdata)
            discountTotalArrayList.add(discountTotal)
        }
        var totalAdd = totalArrayList.sumByDouble { it }
        var totalDiscount = discountTotalArrayList.sumByDouble { it }
        return totalAdd - totalDiscount

    }


    fun calculateTotalCostForRejected(pendingOrder: List<RejectedOrderResponse.ITEMSItem>): Double {
        var pendingOrderdata = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            pendingOrderdata.add(Pendingdata)
        }
        return pendingOrderdata.sumByDouble { it }
    }

    fun CalculateTotalDiscountForRejected(pendingOrder: List<RejectedOrderResponse.ITEMSItem>): Double {
        var pendingOrderdata = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            var percentagCal = (Pendingdata * pendingOrder[i].APPROVEDDISC!!) / 100
            pendingOrderdata.add(percentagCal)
        }
        return pendingOrderdata.sumByDouble { it }
    }


    fun CalculateNetPaymentForRejected(pendingOrder: List<RejectedOrderResponse.ITEMSItem>): Double {
        var totalArrayList = ArrayList<Double>()
        var discountTotalArrayList = ArrayList<Double>()
        for (i in pendingOrder.indices) {
            var Pendingdata = pendingOrder[i].PRICE * pendingOrder[i].QTY
            var discountTotal = (Pendingdata * pendingOrder[i].APPROVEDDISC!!) / 100
            totalArrayList.add(Pendingdata)
            discountTotalArrayList.add(discountTotal)
        }
        var totalAdd = totalArrayList.sumByDouble { it }
        var totalDiscount = discountTotalArrayList.sumByDouble { it }
        return totalAdd - totalDiscount

    }
}