public class Preferences {
    public static func value<T>(forKey key: String, defaultValue: T) -> T{
        let preferences = UserDefaults.standard
        return preferences.object(forKey: key) == nil ? defaultValue : preferences.object(forKey: key) as! T
    }

    public static func value(forKey key: String, value: Any){
        UserDefaults.standard.set(value, forKey: key)
    }
}
