import UIKit
import core_shared

public class GenericItemAdapter: UICollectionView {
    public var skeleton: GenericItem? = nil
    public var items = [GenericItem]()
    public var itemsCache = [GenericItem]()
    public var isLastPage = false
    public var itemSize = CGSize(width: 0, height: 0)
    public var spacing = CGFloat(16)
    public var background: UIColor = .white
    public var enableShadow = true
    public var shadowOpacity: Float = 0.3
    public var fetchData: (() -> (Void))? = nil
    public var onSelected: ((GenericItem) -> (Void))? = nil
    public var onScrolled: ((UIScrollView) -> (Void))? = nil

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.register(GenericItemViewController.nib(), forCellWithReuseIdentifier: GenericItemViewController.identifier)
        self.delegate = self
        self.dataSource = self
    }
    
    public func collectionView(_ collectionView: UICollectionView, willDisplay c: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if indexPath.row == items.count - 1 && !isLastPage {
            if let action = fetchData {
                action()
            }
        }
    }
    
    public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if let action = onSelected {
            action(items[indexPath.row])
        }
    }
    
    public func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if items.count > AppConstant.shared.LIST_LIMIT * 3 {
            var indexes = [IndexPath]()
            var visibles = [Int]()
            for i in self.indexPathsForVisibleItems {
                visibles.append(i.row)
                if self.items[i.row].id == 0 {
                    self.items[i.row] = self.itemsCache[i.row]
                    indexes.append(i)
                }
            }
            var range = [Int]()
            if visibles.min()! != 0 {
                var count = 0
                for i in (visibles.min()!-Int(AppConstant.shared.LIST_LIMIT))...(visibles.min()!-1) {
                    if count == AppConstant.shared.LIST_LIMIT {
                        break
                    } else if i < 0 {
                        continue
                    }
                    range.append(i)
                    count += 1
                }
            }
            let lastIndex = items.count
            if visibles.max()! != lastIndex {
                var count = 0
                for i in (visibles.max()!+1)...lastIndex {
                    if count == AppConstant.shared.LIST_LIMIT {
                        break
                    }
                    range.append(i)
                    count += 1
                }
            }
            for i in 0...items.count - 1 {
                if !visibles.contains(i) && !range.contains(i) {
                    if let skl = self.skeleton {
                        self.items[i] = skl
                    }
                    indexes.append(IndexPath(item: i, section: 0))
                }
            }
            if indexes.count > 0 {
                UIView.performWithoutAnimation {
                    self.reloadItems(at: indexes)
                }
            }
        }
    }
    
    public func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if let action = onScrolled {
            action(scrollView)
        }
    }
}

// MARK: - DataSource
extension GenericItemAdapter: UICollectionViewDataSource {
    public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return items.count
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfSections section: Int) -> Int {
        return 1
    }

    public func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: GenericItemViewController.identifier, for: indexPath) as! GenericItemViewController
        cell.configure(with: items[indexPath.row])
        cell.contentView.backgroundColor = self.background
        cell.contentView.layer.cornerRadius = 8.0
        cell.contentView.layer.masksToBounds = true
        
        if enableShadow {
            layer.shadowColor = UIColor.lightGray.cgColor
            layer.shadowOffset = CGSize(width: 0, height: 2)
            layer.shadowRadius = 8.0
            layer.shadowOpacity = shadowOpacity
            layer.masksToBounds = false
            layer.shadowPath = UIBezierPath(roundedRect: bounds, cornerRadius: cell.contentView.layer.cornerRadius).cgPath
            layer.backgroundColor = UIColor.clear.cgColor
        }
        
        return cell
    }
}

// MARK: - DelegateFlowLayout
extension GenericItemAdapter: UICollectionViewDelegateFlowLayout {
    public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return itemSize
    }
    
    public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return spacing
    }
}

// MARK: - Action
extension GenericItemAdapter {
    public func clear() {
        var indexes = [IndexPath]()
        for (i,_) in items.enumerated() {
            indexes.append(IndexPath(row: i, section: 0))
        }
        items.removeAll()
        itemsCache.removeAll()
        UIView.performWithoutAnimation {
            deleteItems(at: indexes)
        }
        showShimmer(false)
    }
    
    public func showSkeletons(_ count: Int = Int(AppConstant.shared.LIST_LIMIT)) {
        clear()
        var indexes = [IndexPath]()
        for i in 0...count-1 {
            if let skl = skeleton {
                items.append(skl)
                indexes.append(IndexPath(row: i, section: 0))
            }
        }
        UIView.performWithoutAnimation {
            insertItems(at: indexes)
        }
        showShimmer(true)
    }
    
    public func addItems(_ count: Int, _ process: () -> (Void)) {
        if count > 0 {
            if itemsCache.count == 0 && items.count > 0 {
                clear()
            }
            let lastIndex = items.count - 1
            process()
            if count < AppConstant.shared.LIST_LIMIT {
                isLastPage = true
            }
            var indexes = [IndexPath]()
            for i in 1...count {
                indexes.append(IndexPath(row: lastIndex + i, section: 0))
            }
            UIView.performWithoutAnimation {
                insertItems(at: indexes)
            }
        }
    }
}
