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
        self.o2mc = O2MC(endpoint: "http://127.0.0.1:5000/events")
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func BtnTouchCreateEvent(_ sender: Any) {
        os_log("created event", self._logTopic)
        
        self.o2mc.track(self.eventNameTextField.text)
    }
    
    @IBAction func BtnTouchResetTracking(_ sender: Any) {
        os_log("reset tracking", self._logTopic)
        
        self.o2mc.tracker.clearFunnel()
    }

    @IBAction func BtnTouchStopTracking(_ sender: Any) {
        os_log("stop tracking", self._logTopic)

        self.o2mc.stop()
    }
}

