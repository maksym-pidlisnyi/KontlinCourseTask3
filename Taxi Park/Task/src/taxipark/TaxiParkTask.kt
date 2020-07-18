package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> = allDrivers.filter { it -> it !in trips.map {
    trip -> trip.driver } }.toSet()

fun TaxiPark.findFakeDriver1(): Set<Driver> = allDrivers.filter { driver -> trips.none{ it.driver == driver} }.toSet()

fun TaxiPark.findFakeDriver3(): Set<Driver> = allDrivers - (trips.map { it.driver })

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> = allPassengers.filter { it -> trips.filter {
    trip -> it in trip.passengers }.count() >= minTrips }.toSet()

fun TaxiPark.findFaithfulPassengers1(minTrips: Int): Set<Passenger> =
        allPassengers
                .filter { passenger ->
                    trips.count { passenger in it.passengers } >= minTrips
                }
                .toSet()

fun TaxiPark.findFaithfulPassengers2(minTrips: Int): Set<Passenger> =
        trips
                .flatMap ( Trip::passengers )
                .groupBy { passenger ->  passenger }
                .filterValues { group -> group.size >= minTrips }
                .keys

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> = allPassengers.filter { it -> trips.filter {
    trip -> it in trip.passengers && trip.driver == driver }.count() > 1 }.toSet()


/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        allPassengers
                .filter { passenger ->
                    trips.filter { it -> passenger in it.passengers && it.discount == null }.count() <
        trips
                .filter { it ->
                    passenger in it.passengers && it.discount != null }.count()
}.toSet()

fun TaxiPark.findSmartPassengers1(): Set<Passenger> {
    val (tripsWithDiscount, tripsWithoutDiscount) =
            trips.partition { it.discount != null }
    return allPassengers
            .filter { passenger ->
                tripsWithDiscount.count { passenger in it.passengers } >
                        tripsWithoutDiscount.count { passenger in it.passengers }
            }.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if(trips.isEmpty())
        return null
    val maxDuration = trips.map { it.duration }.max() ?: 0
    val tripsMap = HashMap<Int, IntRange>()
    for (i in 0..maxDuration step 10) {
        val range = IntRange(i, i+9)
        val tripsInRange = trips.filter { it.duration in range }.count()
        tripsMap[tripsInRange] = range
    }
    return tripsMap[tripsMap.toSortedMap().lastKey()]
}

fun TaxiPark.findTheMostFrequentTripDurationPeriod1(): IntRange? =
        trips
                .groupBy {
                    val start = it.duration / 10 * 10
                    val end = start + 9
                    start..end
                }
                .maxBy { (_, group) -> group.size }
                ?.key

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if(trips.isEmpty())
        return false
    val allTrips = trips.map { it.cost }.sum()
    val mapOfCosts = trips.groupBy { it.driver }.mapValues {
        ( _, trips ) -> trips.sumByDouble { it.cost }
    }.toList().sortedByDescending { (_, item) -> item }.toMap()
    var sum = 0.0
    var drivers = 0
    for (i in mapOfCosts.values) {
        drivers++
        sum += i
        if (sum >= allTrips * 0.8)
            break
    }
    return drivers <= allDrivers.size * 0.2
}