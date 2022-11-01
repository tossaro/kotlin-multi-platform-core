package multi.platform.core.example.domain.profile.usecase

import multi.platform.core.example.domain.profile.ProfileRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProfileUseCase : KoinComponent {
    private val profileRepository: ProfileRepository by inject()
    suspend operator fun invoke(accessToken: String?) = profileRepository.getProfile(accessToken)
}