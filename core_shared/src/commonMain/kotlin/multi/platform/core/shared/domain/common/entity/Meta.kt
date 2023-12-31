package multi.platform.core.shared.domain.common.entity

import kotlinx.serialization.Serializable
import multi.platform.core.shared.external.enum.ErrorEnum

@Serializable
data class Meta(
    var code: String? = null,
    var message: String? = null,
    var errorType: ErrorEnum? = null,
    var error: String? = null,
) {
    init {
        when (code) {
            "9005" -> {
                errorType = ErrorEnum.TokenError
                error = "Akses ditolak"
            }

            "8004" -> {
                errorType = ErrorEnum.DataNotFound
                error = "Data tidak ditemukan"
            }

            "8101" -> {
                errorType = ErrorEnum.AuthError
                error = "Akun telah terdaftar"
            }

            "8106" -> {
                errorType = ErrorEnum.DataNotFound
                error = "Tipe akun salah"
            }

            "8107" -> {
                errorType = ErrorEnum.DataNotFound
                error = "Izin tidak ditemukan"
            }

            "8120" -> {
                errorType = ErrorEnum.TooManyRetryEmailVerification
                error = "Batas maksimum kirim ulang"
            }

            "8121" -> {
                errorType = ErrorEnum.TooManyRetryOTPRegistration
                error = "Pendaftaran tidak bisa dilanjutkan sementara"
            }

            "8126" -> {
                errorType = ErrorEnum.TooManyChangeEmailVerification
                error = "Ubah email gagal"
            }

            "8123" -> {
                errorType = ErrorEnum.RequestOTPErrorRegistration
                error = "OTP tidak dapat diproses"
            }

            "8201", "8202", "8203", "8204" -> {
                errorType = ErrorEnum.AuthError
                error = "Kesalahan otentikasi"
            }

            "8205" -> {
                errorType = ErrorEnum.AccountLocked
                error = "Akun terkunci sementara"
            }

            "8207" -> {
                errorType = ErrorEnum.RequestOTPErrorLogin
                error = "OTP tidak dapat diproses"
            }

            "8208" -> {
                errorType = ErrorEnum.TooManyRetryOTPLogin
                error = "Akun terkunci sementara"
            }
        }
    }
}