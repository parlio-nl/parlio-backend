package nl.parlio.api.tweedekamer.graphql.controllers

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class EchoController {

    @DgsQuery
    fun echo(@InputArgument msg: String): String {
        return msg
    }
}