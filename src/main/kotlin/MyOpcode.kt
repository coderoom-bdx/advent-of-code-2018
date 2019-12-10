object MyOpcode {
    fun decode(input: List<Int>): List<Int> {
        var iterations = input.size / 4
        var inputTampon = input
        for(index in 0 until iterations){
            inputTampon = decodeFromIndex(index * 4,inputTampon)
        }
        return inputTampon
    }

    private fun decodeFromIndex(start:Int,input: List<Int>): List<Int> {
        if (input[start] == 99) {
            return input
        }
        val indice1 = input[start + 1]
        val indice2 = input[start + 2]
        val indiceResult = input[start + 3]
        val result = if (input[start] == 1) {
            input[indice1] + input[indice2]
        } else {
            input[indice1] * input[indice2]
        }
        val listResult = input.toMutableList()
        listResult[indiceResult] = result
        return listResult
    }

}
