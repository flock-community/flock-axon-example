type ItemQueryDto {
    id: String,
    title: String,
    price: String
}

type QueryProblem {
    reason: String
}

endpoint GetBasketIds GET /basket -> {
    200 -> String[]
}

endpoint GetItems GET /basket/{basketId: String}/items -> {
    200 -> ItemQueryDto[]
    400 -> QueryProblem
    500 -> QueryProblem
}
