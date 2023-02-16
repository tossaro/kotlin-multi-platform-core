public class AppInfo {
    public static var appName: String {
        return readFromInfoPlist(withKey: "CFBundleName") ?? "(unknown app name)"
    }

    public static var version: String {
        return readFromInfoPlist(withKey: "CFBundleShortVersionString") ?? "(unknown app version)"
    }

    public static var appVersion: String {
        return self.version + "-" + self.scheme
    }

    public static var buildNumber: String {
        return readFromInfoPlist(withKey: "CFBundleVersion") ?? "(unknown build number)"
    }

    public static var scheme: String {
        return readFromInfoPlist(withKey: "SCHEME") ?? "(unknown scheme)"
    }

    public static var minimumOSVersion: String {
        return readFromInfoPlist(withKey: "MinimumOSVersion") ?? "(unknown minimum OSVersion)"
    }

    public static var copyrightNotice: String {
        return readFromInfoPlist(withKey: "NSHumanReadableCopyright") ?? "(unknown copyright notice)"
    }

    public static var bundleIdentifier: String {
        return readFromInfoPlist(withKey: "CFBundleIdentifier") ?? "(unknown bundle identifier)"
    }

    public static var developer: String { return "Hamzah Tossaro" }

    public static func readFromInfoPlist(withKey key: String) -> String? {
        return Bundle.main.infoDictionary?[key] as? String
    }
}
