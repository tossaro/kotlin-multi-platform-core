Pod::Spec.new do |spec|
    spec.name                  = 'core'
    spec.version               = '0.8.1'
    spec.homepage              = 'https://gitlab.com/tossaro/kotlin-multi-platform-core'
    spec.source                = { :git => 'https://gitlab.com/tossaro/kotlin-multi-platform-core.git', :tag => spec.version.to_s }
    spec.license               = { :type => 'MIT', :file => 'LICENSE' }
    spec.summary               = 'Provide base constructor / abstract for simplify code structure'
    spec.authors      		   =  { 'tossaro' => 'hamzah.tossaro@gmail.com' }
    spec.source_files 		   = "core_ios/core/Classes/**/*.{swift}"
    spec.resources             = "core_ios/core/Resources/**/*.{gif,png,jpeg,jpg,storyboard,xib,xcassets}"
    spec.libraries             = 'c++'
    spec.ios.deployment_target = '14.1'
    spec.static_framework      = true
    spec.dependency 'core_shared'
    spec.dependency 'SDWebImage'
end
