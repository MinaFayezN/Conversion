package dev.mina.conversion.data

import javax.inject.Inject
import javax.inject.Named

interface DetailsRepo {
    suspend fun getTimeFrameRates(source: String): TimeFrameRates
}

class DetailsRepoImpl @Inject constructor(
    private val dataSource: FixerAPI,
    @Named("CurrentDayDate")
    private val endDate: String,
    @Named("PastThreeDaysDate")
    private val startdate: String,
) : DetailsRepo {

    override suspend fun getTimeFrameRates(source: String): TimeFrameRates =
        dataSource.getTimeFrameRates(startdate, endDate, source)
}