type ItemDto {
    title: String,
    price: Number
}

type CommandProblem {
    reason: String
}

endpoint GetNewBasket POST /basket -> {
    200 -> UUID
}

endpoint PostItem POST ItemDto /basket/{basketId: String}/items -> {
    200 -> Unit
    400 -> CommandProblem
}

endpoint DeleteItem DELETE /basket/{basketId: String}/items/{itemId: String} -> {
    200 -> Unit
    400 -> CommandProblem
}
