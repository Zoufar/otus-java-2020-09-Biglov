package hw24di.config2;

import hw24di.appcontainer.api.AppComponent;
import hw24di.appcontainer.api.AppComponentsContainerConfig;
import hw24di.services.*;


@AppComponentsContainerConfig(order = 1)
public class   AppConfig2 {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

}
