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
    var o2mc : O2MC!
    var _logTopic: OSLog!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self._logTopic = OSLog(subsystem: "io.o2mc.app-swift", category: "testapp-swift")
        self.o2mc = O2MC("app-swift", "http://127.0.0.1:5000/events", 10, true)
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func BtnTouchCreateEvent(_ sender: Any) {
        os_log("created event", self._logTopic)
        
        self.o2mc.tracker.track(self.eventNameTextField.text)
    }
    
    @IBAction func BtnTouchCreateAlias(_ sender: Any) {
        os_log("created alias", self._logTopic)
        
        self.o2mc.tracker.createAlias(self.eventNameTextField.text)
    }
    
    @IBAction func BtnTouchSetIdentity(_ sender: Any) {
        os_log("created identity", self._logTopic)
        
        self.o2mc.tracker.identify(self.eventNameTextField.text)
    }
    
    @IBAction func BtnTouchResetTracking(_ sender: Any) {
        os_log("reset tracking", self._logTopic)
        
        self.o2mc.tracker.clearFunnel()
    }

    @IBAction func BtnTouchStopTracking(_ sender: Any) {
        os_log("stop tracking", self._logTopic)

        self.o2mc.tracker.stop()
    }
}

