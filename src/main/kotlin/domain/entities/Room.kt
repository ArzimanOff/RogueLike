package domain.entities

/**
 * Класс хранящий параметры отдельной комнаты
 * @param topLeftX Координата X верхнего-левого угла комнаты
 * @param topLeftY Координата X верхнего-левого угла комнаты
 * @param width Ширина комнаты
 * @param height Высота комнаты (Длина)
 */
data class Room(
    val topLeftX: Int,
    val topLeftY: Int,
    val width: Int,
    val height: Int
) {
    val left = minOf(btl.first, btr.first, bbl.first, bbr.first)
    val right = maxOf(btl.first, btr.first, bbl.first, bbr.first)
    val top = maxOf(btl.second, btr.second, bbl.second, bbr.second)
    val bottom = minOf(btl.second, btr.second, bbl.second, bbr.second)

    val border: List<Pair<Int, Int>>
        get() {
            val list = mutableListOf<Pair<Int, Int>>()
            for (i in btl.first..btr.first) {
                list.add(
                    Pair(
                        i,
                        btl.second
                    )
                )
                list.add(
                    Pair(
                        i,
                        bbl.second
                    )
                )
            }

            for (i in btl.second+1..<bbl.second) {
                list.add(
                    Pair(
                        btl.first,
                        i
                    )
                )
                list.add(
                    Pair(
                        btr.first,
                        i
                    )
                )
            }
            return list
        }
    val btl
        get() = Pair(
            this.tlX - 1,
            this.tlY - 1
        )
    val btr
        get() = Pair(
            this.trX + 1,
            this.trY - 1
        )
    val bbl
        get() = Pair(
            this.blX - 1,
            this.blY + 1
        )
    val bbr
        get() = Pair(
            this.brX + 1,
            this.brY + 1
        )

    val tlX
        get() = this.topLeftX
    val tlY
        get() = this.topLeftY
    val blX
        get() = this.topLeftX
    val blY
        get() = this.topLeftY + this.height - 1
    val trX
        get() = this.topLeftX + this.width - 1
    val trY
        get() = this.topLeftY
    val brX
        get() = this.topLeftX + this.width - 1
    val brY
        get() = this.topLeftY + this.height - 1

}
