package com.reference.model.service

import com.reference.model.entity.Member
import com.reference.model.repo.MemberRepository
import com.reference.model.requestBodies.LoginRequest
import com.reference.model.requestBodies.RegistRequest
import com.reference.model.requestBodies.UpdateRequest
import com.reference.model.responseBodies.LoginResponse
import com.reference.model.responseBodies.RegistResponse
import com.reference.model.responseBodies.Result
import com.reference.model.responseBodies.UpdateResponse
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun login(loginRequest: LoginRequest): LoginResponse? {
        val memberResult = memberRepository.findByEmail(loginRequest.email)
        if (memberResult.isEmpty) return null
        if(!BCrypt.checkpw(loginRequest.password, memberResult.get().password)) return null
        return LoginResponse(Result.SUCCESS, memberResult.get().id, memberResult.get().name, memberResult.get().email, memberResult.get().address)
    }

    @Transactional
    fun update(updateRequest: UpdateRequest): UpdateResponse? {
        val memberResult = memberRepository.findById(updateRequest.id)
        if (memberResult.isEmpty) return null

        if (updateRequest.name.isNotEmpty()) memberResult.get().name = updateRequest.name
        val passwordHashed = BCrypt.hashpw(updateRequest.password, BCrypt.gensalt())
        updateRequest.password = passwordHashed
        if (updateRequest.password.isNotEmpty()) memberResult.get().password = updateRequest.password
        memberResult.get().address = updateRequest.address

        return UpdateResponse(Result.SUCCESS, memberResult.get().id, memberResult.get().name, memberResult.get().email, memberResult.get().address)
    }

    fun countByEmail(email: String): Int {
        return memberRepository.countByEmail(email)
    }

    fun regist(registRequest: RegistRequest): RegistResponse {
        val count = memberRepository.countByEmail(registRequest.email)
        if (count > 0) return RegistResponse()
        val member = Member()
        member.email = registRequest.email
        member.name = registRequest.name
        member.password = BCrypt.hashpw(registRequest.password, BCrypt.gensalt())
        member.address = registRequest.address

        val result = memberRepository.save(member)
        return RegistResponse(Result.SUCCESS, result.id, result.email, result.name, result.address)
    }
}