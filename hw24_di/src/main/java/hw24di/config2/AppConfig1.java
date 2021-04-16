package hw24di.config2;

import hw24di.appcontainer.api.AppComponent;
import hw24di.appcontainer.api.AppComponentsContainerConfig;
import hw24di.services.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig1 {

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }

}
