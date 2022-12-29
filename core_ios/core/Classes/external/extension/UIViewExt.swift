import UIKit

extension UIView {
    public func showFulLoading(_ show: Bool) {
        if show {
            let blurLoader = BlurLoader(frame: frame)
            self.addSubview(blurLoader)
        } else {
            if let blurLoader = subviews.first(where: { $0 is BlurLoader }) {
                blurLoader.removeFromSuperview()
            }
        }
    }
    
    public func showShimmer(_ show: Bool) {
        if !show {
            layer.mask = .none
        } else {
            let gradient = CAGradientLayer()
            gradient.startPoint = CGPoint(x: 0, y: 0)
            gradient.endPoint = CGPoint(x: 1, y: -0.02)
            gradient.frame = CGRect(x: 0, y: 0, width: bounds.size.width*3, height: bounds.size.height)

            let solid = UIColor(white: 1, alpha: 1).cgColor
            let clear = UIColor(white: 1, alpha: 0.5).cgColor
            gradient.colors     = [ solid, solid, clear, clear, solid, solid ]
            gradient.locations  = [ 0,     0.3,   0.45,  0.55,  0.7,   1     ]

            let theAnimation = CABasicAnimation(keyPath: "transform.translation.x")
            theAnimation.duration = 1.5
            theAnimation.repeatCount = Float.infinity
            theAnimation.autoreverses = false
            theAnimation.isRemovedOnCompletion = false
            theAnimation.fillMode = CAMediaTimingFillMode.forwards
            theAnimation.fromValue = -frame.size.width * 1.5
            theAnimation.toValue =  0
            gradient.add(theAnimation, forKey: "animateLayer")
            
            layer.mask = gradient
        }
      }
}
