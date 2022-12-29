public class Core<KoinApp, Koin> {
    public init(_ _sharedKeyPaths: [PartialKeyPath<Koin>], _ _koinApp: KoinApp, _ _koin: Koin) {
        sharedKeyPaths = _sharedKeyPaths
        koinApp = _koinApp
        koin = _koin
    }
    
    public var koin: Koin
    public var koinApp: KoinApp
    private var sharedKeyPaths: [PartialKeyPath<Koin>] = []
    
    public func inject<T>() -> T {
        for partialKeyPath in sharedKeyPaths {
            guard let keyPath = partialKeyPath as? KeyPath<Koin, T> else { continue }
            return koin[keyPath: keyPath]
        }

        fatalError("\(T.self) is not registered")
    }
}
