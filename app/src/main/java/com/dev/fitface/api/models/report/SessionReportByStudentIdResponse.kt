package com.dev.fitface.api.models.report

import com.dev.fitface.api.models.BaseResponse

class SessionReportByStudentIdResponse: BaseResponse() {
    var data: List<ReportCheckIn>? = null
}

