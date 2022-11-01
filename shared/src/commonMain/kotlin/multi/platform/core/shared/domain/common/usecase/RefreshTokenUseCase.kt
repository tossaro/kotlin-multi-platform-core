package multi.platform.core.shared.domain.common.usecase

import multi.platform.core.shared.domain.common.CommonRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshTokenUseCase : KoinComponent {
    private val commonRepository: CommonRepository by inject()
    suspend operator fun invoke(refreshToken: String, phone: String) =
        commonRepository.refreshToken(refreshToken, phone)
}