package presentation

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger

class Presenter {
    val terminalFactory = DefaultTerminalFactory()
        .setInitialTerminalSize(TerminalSize(124, 49))
        .setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode)
        .createTerminal()

    val screen: Screen = TerminalScreen(terminalFactory)
}