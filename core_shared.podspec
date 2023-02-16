Pod::Spec.new do |spec|
    spec.name                     = 'core_shared'
    spec.version                  = '0.8.0'
    spec.homepage                 = 'https://gitlab.com/tossaro/kotlin-multi-platform-core'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Provide base constructor / abstract for simplify code structure'
    spec.vendored_frameworks      = 'core_shared/build/XCFrameworks/release/core_shared.xcframework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.1'
end