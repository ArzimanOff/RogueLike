package domain.entities

data class Level(
    var value: Int = 1
){
    fun increaseLevel(){
        if (value<21){
            value++
        }
    }
    companion object {
        const val MAX_LEVEL = 21
    }
}