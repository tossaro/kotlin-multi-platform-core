import UIKit

public extension String {
    func strikeThrough() -> NSAttributedString {
        return NSAttributedString(
            string: self,
            attributes: [.strikethroughStyle: NSUnderlineStyle.single.rawValue]
        )
    }
}
