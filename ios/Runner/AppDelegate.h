#import <Flutter/Flutter.h>
#import <UIKit/UIKit.h>

#define kSDKKey      @"bb2bD7qXUptIQkhprDt53ZfmjdBXMFEQtQnp"
#define kSDKSecret   @"rWitvIADSG2rmBXWkpIDW0iF98u6fILaDeUf"
#define kSDKDomain   @"zoom.us"

//@interface AppDelegate : FlutterAppDelegate
//
//@end

@interface AppDelegate :  UIResponder <UIApplicationDelegate, UIAlertViewDelegate, MobileRTCPremeetingDelegate>

@property (strong, nonatomic) UIWindow *window;

- (UIViewController *)topViewController;
@end
