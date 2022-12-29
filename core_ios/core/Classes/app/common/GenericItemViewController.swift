import UIKit
import core_shared

public class GenericItemViewController: UICollectionViewCell {
    @IBOutlet var fullImageView: UIImageView!
    @IBOutlet var topImageView: UIImageView!
    @IBOutlet var leftImageView: UIImageView!
    @IBOutlet var titleOverlayView: UILabel!
    @IBOutlet var subtitleOverlayView: UILabel!
    @IBOutlet var titleView: UILabel!
    @IBOutlet var subtitleView: UILabel!
    @IBOutlet var discountMiddleView: UILabel!
    @IBOutlet var priceMiddleView: UILabel!
    @IBOutlet var priceMiddleUnitView: UILabel!
    @IBOutlet var containerLeftConstraint: NSLayoutConstraint!
    
    static let identifier = "GenericItemView"
    static func nib() -> UINib {
        return UINib(nibName: "GenericItemView", bundle: nil)
    }

    public override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    public func configure(with item: GenericItem) {
        setupImage(with: item)
        setupTitle(with: item)
        setupPrice(with: item)
    }
    
    private func setupImage(with item: GenericItem) {
        if let it = item.fullImage {
            if (item.id == 0 && it == " "){
                fullImageView?.image = nil
                fullImageView?.backgroundColor = .grey20
            } else {
                fullImageView?.backgroundColor = .clear
                fullImageView?.downloaded(from: it)
                fullImageView?.contentMode = .scaleAspectFill
            }
        } else {
            fullImageView?.removeFromSuperview()
        }
        if let it = item.topImage {
            if (item.id == 0 && it == " "){
                topImageView?.image = nil
                topImageView?.backgroundColor = .grey20
            } else {
                topImageView?.backgroundColor = .clear
                topImageView?.downloaded(from: it)
                topImageView?.contentMode = .scaleAspectFill
            }
        } else {
            topImageView?.removeFromSuperview()
        }
        if let it = item.leftImage {
            if (item.id == 0 && it == " "){
                leftImageView?.image = nil
                leftImageView?.backgroundColor = .grey20
            } else {
                leftImageView?.backgroundColor = .clear
                leftImageView?.downloaded(from: it)
                leftImageView?.contentMode = .scaleAspectFill
            }
        } else {
            leftImageView?.removeFromSuperview()
            containerLeftConstraint?.constant = 8
        }
    }

    private func setupTitle(with item: GenericItem) {
        if let it = item.titleOverlay {
            titleOverlayView?.text = it
            titleOverlayView?.backgroundColor = item.id == 0 && it == " " ? .grey20 : .clear
        } else {
            titleOverlayView?.removeFromSuperview()
        }
        if let it = item.subtitleOverlay {
            subtitleOverlayView?.text = it
            subtitleOverlayView?.backgroundColor = item.id == 0 && it == " " ? .grey20 : .clear
        } else {
            subtitleOverlayView?.removeFromSuperview()
        }
        if let it = item.title {
            titleView?.text = it
            titleView?.backgroundColor = item.id == 0 && it == " " ? .grey20 : .clear
        } else {
            titleView?.removeFromSuperview()
        }
        if let it = item.subtitle {
            subtitleView?.text = it
            subtitleView?.backgroundColor = item.id == 0 && it == " " ? .grey20 : .clear
        } else {
            subtitleView?.removeFromSuperview()
        }
    }

    private func setupPrice(with item: GenericItem) {
        if let it = item.middleDiscount {
            discountMiddleView?.attributedText = it.strikeThrough()
            discountMiddleView?.backgroundColor = item.id == 0 && it == " " ? .grey20 : .clear
        } else {
            discountMiddleView?.removeFromSuperview()
        }
        if let it = item.middlePrice {
            priceMiddleView?.text = it
            priceMiddleView?.backgroundColor = item.id == 0 && it == " " ? .grey20 : .clear
        } else {
            priceMiddleView?.removeFromSuperview()
        }
        if let it = item.middlePriceUnit {
            priceMiddleUnitView?.text = it
            priceMiddleUnitView?.backgroundColor = item.id == 0 && it == " " ? .grey20 : .clear
        } else {
            priceMiddleUnitView?.removeFromSuperview()
        }
    }

}
