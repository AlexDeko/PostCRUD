package com.postcrud.route

import com.postcrud.repository.PostRepository
import io.ktor.routing.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein
import java.nio.file.Files.delete

class RoutingV1 {
    fun routingV1() {
        route("/api/v1/posts") {
            val repo by kodein().instance<PostRepository>()
//            get {
//                val response = repo.getAll().map { PostResponseDto.fromModel(it) }
//                call.respond(response)
//            }
//            get("/{id}") {
//                val id = call.parameters["id"]?.toLongOrNull()
//                    ?: throw ParameterConversionException("id", "Long")
//                val model = repo.getById(id) ?: throw Kodein.NotFoundException()
//                val response = PostResponseDto.fromModel(model)
//                call.respond(response)
//            }
//            post {
//                val input = call.receive<PostRequestDto>()
//                val model = PostModel(id = input.id, author = input.author, content = input.content)
//                val response = PostResponseDto.fromModel(repo.save(model))
//                call.respond(response)
//            }
//            delete("/{id}") {
//                TODO()
//            }
        }
    }
}