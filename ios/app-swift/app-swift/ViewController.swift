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
    var o2mc : O2MC!
    var _logTopic: OSLog!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if #available(iOS 10.0, *) {
            self._logTopic = OSLog(subsystem: "io.o2mc.app-swift", category: "testapp-swift")
        }
        self.o2mc = O2MC(endpoint: "http://127.0.0.1:5000/events")
        
        self.endpointNameTextField.text = self.o2mc.getEndpoint()
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
        
        self.o2mc.track(self.eventNameTextField.text!)
    }
    
    @IBAction func BtnTouchResetTracking(_ sender: Any) {
        if #available(iOS 10.0, *) {
            os_log("reset tracking", self._logTopic)
        } else {
            NSLog("reset tracking")
        }
        
        self.o2mc.tracker.clearFunnel()
    }

    @IBAction func BtnTouchStopTracking(_ sender: Any) {
        if #available(iOS 10.0, *) {
            os_log("stop tracking", self._logTopic)
        } else {
            NSLog("stop tracking")
        }

        self.o2mc.stop()
    }

    @IBAction func InputEndpointChanged(_ sender: Any) {
        if #available(iOS 10.0, *) {
            os_log("endpoint changed", self._logTopic)
        } else {
            NSLog("endpoint changed")
        }

        self.o2mc.setEndpoint(self.endpointNameTextField.text!)
    }
}

