/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jplus.compare.excel.main;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jplus.compare.excel.frame.StartFrame;

/**
 *
 * @author hyberbin
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        new StartFrame().setVisible(true);
    }
}
