package models

import java.time.chrono.ChronoLocalDateTime

class model(var name : String , var isSelected : Boolean) : Comparable<model> {
    override fun compareTo(other: model): Int {
        return other.name.compareTo(name,true)
    }

}