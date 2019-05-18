# Kunirx: Unidirectional reactive Android architecture

Kunirx is a unidirectional reactive Android architecture built with Kotlin. It imploys a MVI (Model-View-Intent) approach where each UI interaction is mapped to a testable and self-contained `UIAction`. Each `UIAction` provides a reactive style (with `RxJava`) callback for business logic and provides a well-defined `reduce` callback to update the UI state. The framework provides the necessary constructs to quickly establish an easy to maintain and consistent app.

## Usage
First we need to define a `UIState` object and a corresponding `Activity`/`Fragment`. We will create simple `Activity` that has a button to increment a counter and update the UI.

```
data class MyActivityUIState(
  val counter: Int,
  val isUpdating: Boolean, // In case we want to update UI while the counter is being incremented
  val error: Throwable // In case we want to report an error back to the UI
): UIState

class MyActivity: UIActivity<MyActivity>{
  ...
}

```

In order to increment the counter we need to map the button click with a `UIAction`. We can do so by creating a simple yet powerful and testable `UIAction` as follows:

```
class IncrementClickUIAction: UIAction<MyActivityUIState, Input, ClickMutator> {

    // This is set as the counter value in the UIState
    class Input(val currentValue: Int) : UIAction.Input

    // Callback for the business logic, returns any updates to reduce() function as 
    // UIAction.UIStateMutator
    override fun execute(input: Input): Observable<ClickMutator> {
        return Observable
            .just<ClickMutator>(Success(input.currentValue + 1))
            .startWith(Loading)
            .onErrorReturn { throwable -> Error(throwable) }
    }

    sealed class ClickMutator : UIAction.UIStateMutator {
        object Loading : ClickMutator()
        class Success(val newValue: Int) : ClickMutator()
        class Error(val throwable: Throwable) : ClickMutator()
    }

    // Callback for updating the UI.
    override fun reduce(
        uiState: MyActivityUIState,
        uiStateMutator: ClickMutator
    ): CounterActivityUIState {
        return when (uiStateMutator) {
            is Loading -> uiState
            is Success -> uiState.copy(counterValue = uiStateMutator.newValue)
            is Error -> uiState.copy(
        }
    }
}
```
That's it! The updated `UIState` will call the `render()` function callback on the `MyActivity` and the UI can be rendered consistently. The `Activity` in this case only needs to provide 3 callbacks; 1 for registering the `UIAction`, triggering the `UIAction` and handling the results:

```
class MyActivity: UIActivity<MyActivity>{

  // Register the UIAction
  override fun uiActionHandlerConfiguration() = object: UIActionHandler.Configuration(
    uiActions = listOf(IncrementClickUIAction)
  )

  //Trigger the UIAction
  override fun uiActionInputObservable(): Observable<UIAction.Input> {
    // Works great with RxBindings
    return incrementButton
              .clicks()
               .map { IncrementClickUIAction.Input(currentValue = uiState.counterValue) },
  }
  
  override fun render(uiState: CounterActivityUIState) {
      counterButton.text = uiState.counterValue.toString()
  }  
}
```

Check out the [simple counter app](https://github.com/jaxvy/kunirx/tree/master/counter-app) and the [To-dos app](https://github.com/jaxvy/kunirx/tree/master/todo-app) to see more usage examples, unit tests and ease of managing `UIAction`s with `Dagger2`.
