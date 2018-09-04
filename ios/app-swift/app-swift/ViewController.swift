//
//  ViewController.swift
//  app-swift
//
//  Created by Juan Jose Bernal on 12/07/2018.
//  Copyright © 2018 O2MC. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var eventNameTextField: UITextField!
    @IBOutlet weak var endpointNameTextField: UITextField!
    var _logTopic: OSLog!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if #available(iOS 10.0, *) {
            self._logTopic = OSLog(subsystem: "io.o2mc.app-swift", category: "testapp-swift")
        }
        
        self.endpointNameTextField.text = O2MC.sharedInstance().getEndpoint()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func BtnTouchCreateEvent(_ sender: Any) {
        if #available(iOS 10.0, *) {
            os_log("created event", self._logTopic)
        } else {
            NSLog("created event")
        }
        
        O2MC.sharedInstance().track(self.eventNameTextField.text!)
    }
    
    @IBAction func BtnTouchResetTracking(_ sender: Any) {
        if #available(iOS 10.0, *) {
            os_log("reset tracking", self._logTopic)
        } else {
            NSLog("reset tracking")
        }
        
        O2MC.sharedInstance().stop()
        O2MC.sharedInstance().resume()
    }

    @IBAction func BtnTouchStopTracking(_ sender: Any) {
        if #available(iOS 10.0, *) {
            os_log("stop tracking", self._logTopic)
        } else {
            NSLog("stop tracking")
        }

        O2MC.sharedInstance().stop()
    }

    @IBAction func InputEndpointChanged(_ sender: Any) {
        if #available(iOS 10.0, *) {
            os_log("endpoint changed", self._logTopic)
        } else {
            NSLog("endpoint changed")
        }

        O2MC.sharedInstance().setEndpoint(self.endpointNameTextField.text!)
    }
}

