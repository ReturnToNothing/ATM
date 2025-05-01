package com.atm;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.atm.States.DEFAULT;
import static com.atm.States.TRANSACTION_COMPLETE;

public class AccountController
{
    @FXML public AnchorPane anchor;

    @FXML public HBox anchorHbox;

    @FXML public ScrollPane verticalScroll;
    @FXML public VBox homeContainer;

    // cardHolder scene

    @FXML public Text username;
    @FXML public Text firstName;

    @FXML public Text accountType;
    @FXML public Text accountPIN;
    @FXML public Text accountBalance;
    @FXML public Text accountCompany;
    @FXML public ImageView accountCompanyIcon;

    @FXML public BorderPane homeBorderPane;
    @FXML public BorderPane walletBorderPane;
    @FXML public BorderPane profileBorderPane;

    // Features scene

    @FXML public AnchorPane addButton;
    @FXML public AnchorPane sendButton;
    @FXML public AnchorPane depositButton;
    @FXML public AnchorPane withdrawButton;

    // Hotbar scene

    @FXML public Button homeButton;
    @FXML public Button walletButton;
    @FXML public Button profileButton;

    public HotbarContext homeContext;
    public HotbarContext walletContext;
    public HotbarContext profileContext;

    private HotbarContext selectedHotbarContext;

    @FXML public VBox transactionContainer;
    @FXML public AnchorPane transactionItem;
    @FXML public AnchorPane transactionDate;

    // summary transaction scene

    @FXML public Button exitButton1;
    @FXML public AnchorPane transactionSummaryScene;
    @FXML public Text sortButton;

    @FXML public VBox transactionContainer1;
    @FXML public AnchorPane transactionItem1;
    @FXML public AnchorPane transactionDate1;

    // feature menu scene

    @FXML public Button exitButton3;
    @FXML public AnchorPane featureMenuScene;

    private String[] sorts = {"All", "Income", "Expenses"};
    private int sortIndex = 0;

    // review scene

    @FXML public AnchorPane transactionReviewScene;
    @FXML public Button exitButton2;
    @FXML public Button sendCashButton1;

    @FXML public Text payerType;
    @FXML public Text payerUsername;
    @FXML public Text payerCompany;
    @FXML public Text payerPIN;
    @FXML public ImageView payerCompanyIcon;
    @FXML public Text payerBalance;

    @FXML public Text payeeType;
    @FXML public Text payeeUsername;
    @FXML public Text payeeCompany;
    @FXML public Text payeePIN;
    @FXML public ImageView payeeCompanyIcon;

    @FXML public Text reviewAmount;
    @FXML public Text reviewDate;

    // transaction completion scene;

    @FXML public AnchorPane transactionCompleteScene;

    // transaction scene

    private PanelContext transactionContext;
    private PanelContext transactionSummaryContext;
    private PanelContext featureMenuContext;

    @FXML public AnchorPane transactionScene;
    @FXML public Button exitButton;
    @FXML public Text seeMoreFButton;
    @FXML public Text seeMoreWButton;
    @FXML public Text seeMoreDButton;

    @FXML public ScrollPane cardHorizontalScroll;
    @FXML public HBox cardContainer;
    @FXML public AnchorPane cardItemContainer;
    @FXML public AnchorPane cardItemBlue;
    @FXML public AnchorPane cardItemGrey;
    @FXML public Text textSelectedCard;

    @FXML public TextField recipientTextField;

    @FXML public ScrollPane payeesHorizontalScroll;
    @FXML public HBox payeesContainer;
    @FXML public AnchorPane payeeItemContainer;
    @FXML public AnchorPane payeeItemBlue;
    @FXML public AnchorPane payeeItemGrey;
    @FXML public Text textSelectedPayee;
    @FXML public Text textSelectedBalance;

    @FXML public TextField amountTextField;
    @FXML public Button sendCashButton;

    private AnchorPane selectedCardNode = null;
    private Card selectedCard = null;
    private Card prevSelectedCard = null;

    private AnchorPane selectedPayeeNode = null;
    private Account selectedPayeeAccount = null;
    private Account prevSelectedPayeeAccount = null;

    private final List<AnchorPane> allCards = new ArrayList<>();
    private final List<AnchorPane> allPayees = new ArrayList<>();

    private Controller controller;
    private Model model;

    private States accountState;
    private States transactionState;

    private double translateX = 0;
    private double translateY = 0;

    public void initialize(Controller controller, Model model)
    {
        this.accountState = States.DEFAULT;
        this.transactionState = States.DEFAULT;

        this.controller = controller;
        this.model = model;

        homeButton.setId("Home-page");
        walletButton.setId("Wallet-page");
        profileButton.setId("Profile-page");

        this.homeContext = new HotbarContext(
                this.homeButton,
                this.homeBorderPane
        );
        this.walletContext = new HotbarContext(
                this.walletButton,
                this.walletBorderPane
        );
        this.profileContext = new HotbarContext(
                this.profileButton,
                this.profileBorderPane
        );

        seeMoreFButton.setId("Features-menu-open");
        seeMoreWButton.setId("Transaction-summary");
        seeMoreDButton.setId("Transaction-list");

        // transaction context

        this.transactionContext = new PanelContext(
                this.transactionScene,
                this.exitButton,
                896.0,
                72.0,
                700.0,
                States.TRANSACTION_PAGE
        );

        // summary context

        this.transactionSummaryContext = new PanelContext(
                this.transactionSummaryScene,
                this.exitButton1,
                896.0,
                72.0,
                400.0,
                States.TRANSACTION_SUMMARY
        );

        // feature context

        this.featureMenuContext = new PanelContext(
                this.featureMenuScene,
                this.exitButton3,
                896.0,
                72.0,
                400.0,
                States.FEATURES_PAGE
        );

        sendButton.setId("Transaction-page-open");
        exitButton.setId("Transaction-page-close");
        recipientTextField.setId("Open-Input");
        amountTextField.setId("Open-Input-Balance");
        sendCashButton.setId("Transaction-page-open");
        sendCashButton1.setId("Transaction-page-open");
        exitButton2.setId("Transaction-page-close");

        exitButton1.setId("Transaction-summary-close");

        exitButton3.setId("Features-menu-close");

        anchor.setTranslateX(464);
        transactionScene.setTranslateY(896);
        transactionReviewScene.setTranslateY(896);

        BindButtons();
        BindScrolls();
        BindTextFields();
        BindHotbarContexts();
        BindPanelContexts();
    }

    public void BindHotbarContexts()
    {
        HotbarContext[] hotbarContexts = {
                this.homeContext, this.walletContext, this.profileContext
        };

        for (HotbarContext hotbarContext : hotbarContexts)
        {
            registerContext(hotbarContext);
        }
    }

    public void BindPanelContexts()
    {
        PanelContext[] panelContexts = {
                this.transactionContext, this.transactionSummaryContext, this.featureMenuContext
        };

        for (PanelContext panelContext : panelContexts)
        {
            registerDraggableAnchor(panelContext);
        }
    }

    public void registerDraggableAnchor(PanelContext panelContext)
    {
        final DoubleProperty dragOffsetY = new SimpleDoubleProperty();
        final BooleanProperty hasRestarted = new SimpleBooleanProperty(false);

        AnchorPane anchorPane = panelContext.panel();
        Button pivotButton = panelContext.pivot();
        Double start = panelContext.start();
        Double end = panelContext.end();
        Double exit = panelContext.exit();
        States state = panelContext.state();

        Timeline slideIn = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchorPane.translateYProperty(), end, new SineInterpolator())
                )
        );

        Timeline slideOut = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchorPane.translateYProperty(), start, new SineInterpolator())
                )
        );

        pivotButton.setOnMousePressed(event ->
        {
            if (this.transactionState.equals(state))
            {
                hasRestarted.set(false);

                if (this.transactionState.equals(state))
                {
                    // If the panel is already open, set its goal Y transition
                    slideIn.playFromStart();
                }

                dragOffsetY.set(event.getSceneY() - anchorPane.getTranslateY());
            }
        });

        pivotButton.setOnMouseReleased(event ->
        {
            if (this.transactionState.equals(state))
            {
                // Snap back if the panel isn't fully closed.
                if (!hasRestarted.get())
                {
                    slideIn.playFromStart();
                }
            }
        });

        pivotButton.setOnMouseDragged(event ->
        {
            double newY = event.getSceneY() - dragOffsetY.get();

            // Clamp to allow bounds
            newY = Math.clamp(newY, end, start);

            slideIn.stop();

            if (newY >= exit && !hasRestarted.get())
            {
                slideOut.playFromStart();
                hasRestarted.set(true);
                this.controller.process(pivotButton.getId());
                return;
            }

            anchorPane.setTranslateY(newY);
        });
    }

    private void registerContext(HotbarContext hotbarContext)
    {
        Button hotbarButton = hotbarContext.hotbarButton();
        BorderPane hotbarBorderPane = hotbarContext.hotbarBorderPane();

        Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(hotbarBorderPane.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(hotbarBorderPane.opacityProperty(), 0, new SineInterpolator())
                )
        );

        hotbarButton.setOnMouseEntered(event ->
        {
            if (hotbarContext != this.selectedHotbarContext)
            {
                hoverIn.playFromStart();
            }
        });

        hotbarButton.setOnMouseExited(event ->
        {
            if (hotbarContext != this.selectedHotbarContext)
            {
                hoverOut.playFromStart();
            }
        });

        hotbarButton.setOnMouseClicked(event ->
        {
            if (hotbarContext != this.selectedHotbarContext)
            {
                // iterate through any hotbarContexts before fading them.

                HotbarContext[] hotbarContexts = {
                        this.homeContext, this.walletContext, this.profileContext
                };

                for (HotbarContext otherHotbarContext : hotbarContexts)
                {
                    if (otherHotbarContext != hotbarContext)
                    {
                        Timeline fadeOutOthers = new Timeline(
                                new KeyFrame(Duration.seconds(.2),
                                        new KeyValue(otherHotbarContext.hotbarBorderPane().opacityProperty(), 0, new SineInterpolator())
                                )
                        );
                        fadeOutOthers.playFromStart();
                    }
                }

                // The selected HotbarContext should instead fade-in
                hoverIn.playFromStart();

                this.selectedHotbarContext = hotbarContext;
                this.controller.process(hotbarButton.getId());
            }
        });
    }

    public void BindScrolls()
    {
        ScrollPane[] scrollPanes = {
                cardHorizontalScroll, payeesHorizontalScroll
        };

        HBox[] hContainers = {
                cardContainer, payeesContainer
        };

        ScrollPane[] vScrollPanes = {
                verticalScroll
        };

        VBox[] vContainers = {
                homeContainer
        };

        // was quite challenging for researching an alternative way for using horizontal scrollable pane...
        // that barely works compared to vertical scroll.
        for (int index = 0; index < vScrollPanes.length; index++)
        {
            ScrollPane currentScrollPane = vScrollPanes[index];
            VBox currentVContainers = vContainers[index];

            int multiply = Integer.parseInt(currentScrollPane.getId());

            currentScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                if (event.getDeltaY() != 0)
                {
                    // Fetch the scroll delta whenever the end-user scrolls up or down.
                    // Up is positive, while down is negative output.
                    double delta = event.getDeltaY();
                    translateY -= delta * multiply;

                    // Clamping to avoid over-scrolling
                    double minTranslate = Math.min(0, currentScrollPane.getHeight() - currentVContainers.getHeight());
                    translateY = Math.max(minTranslate, Math.min(0, translateY));

                    // Animate movement
                    TranslateTransition transition = new TranslateTransition(Duration.millis(150), currentVContainers);
                    transition.setInterpolator(new SineInterpolator());
                    transition.setToY(translateY);
                    transition.play();

                    event.consume(); // Prevent default scroll
                }
            });
        }

        for (int index = 0; index < scrollPanes.length; index++)
        {
            ScrollPane currentScrollPane = scrollPanes[index];
            HBox currentHContainers = hContainers[index];

            int multiply = Integer.parseInt(currentScrollPane.getId());

            currentScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                if (event.getDeltaY() != 0)
                {
                    // Fetch the scroll delta whenever the end-user scrolls up or down.
                    // Up is positive, while down is negative output.
                    double delta = event.getDeltaY();
                    translateX -= delta * multiply;

                    // Clamping to avoid over-scrolling
                    double minTranslate = Math.min(0, currentScrollPane.getWidth() - currentHContainers.getWidth());
                    translateX = Math.max(minTranslate, Math.min(0, translateX));

                    // Animate movement
                    TranslateTransition transition = new TranslateTransition(Duration.millis(150), currentHContainers);
                    transition.setInterpolator(new SineInterpolator());
                    transition.setToX(translateX);
                    transition.play();

                    event.consume(); // Prevent default scroll
                }
            });
        }
    }

    public void BindButtons()
    {

        AnchorPane[] anchors = {
                addButton, sendButton, depositButton, withdrawButton
        };

        Button[] buttons = {
                homeButton, walletButton, profileButton, sendCashButton, sendCashButton1,
                exitButton2
        };

        Text[] texts = {
                seeMoreFButton, seeMoreWButton, seeMoreDButton
        };

        for (Button button : buttons)
        {
            registerButton(button);
        }

        for (Text text : texts)
        {
            registerText(text);
        }

        for (AnchorPane anchorPane : anchors)
        {
            registerAnchorpane(anchorPane);
        }

        registerSort();
    }

    public void BindTextFields()
    {
        TextField[] textFields = {
                recipientTextField, amountTextField
        };

        for (TextField textField : textFields) {
            registerTextField(textField);
        }
    }

    private void hoverNode(Node node, double start, double end)
    {
        // Simplified version compared to other separate methods that strictly require
        // a different type of arguments.

        // Node should at least solve this by accepting any instances.
        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(node.opacityProperty(), start, new SineInterpolator())
                )
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(node.opacityProperty(), end, new SineInterpolator())
                )
        );

        node.setOnMouseEntered(event -> hoverAnimation.playFromStart());
        node.setOnMouseExited(event -> exitAnimation.playFromStart());
    }

    // Similar to hoverButton, but exclusively for any other elements
    private void hoverFadeCards(Node node, Card card)
    {
        Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 0, new SineInterpolator())
                )
        );

        node.setOnMouseEntered(event ->
        {
            if (node != selectedCardNode)
            {
                hoverIn.playFromStart();
            }
        });

        node.setOnMouseExited(event ->
        {
            if (node != selectedCardNode)
            {
                hoverOut.playFromStart();
            }
        });

        node.setOnMouseClicked(event ->
        {
            if (node != selectedCardNode)
            {
                // Fade out the others
                for (AnchorPane cardNode : allCards)
                {
                    if (cardNode != node)
                    {
                        Timeline fadeOutOthers = new Timeline(
                                new KeyFrame(Duration.seconds(0.2),
                                        new KeyValue(cardNode.opacityProperty(), 0, new SineInterpolator()))
                        );
                        fadeOutOthers.playFromStart();
                    }
                }

                // Fade in the selected one
                Timeline fadeInSelected = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(node.opacityProperty(), 1, new SineInterpolator()))
                );

                fadeInSelected.playFromStart();

                this.selectedCardNode = (AnchorPane) node;

                if (this.selectedCard != null)
                {
                    this.prevSelectedCard = this.selectedCard;
                }

                this.selectedCard = card;

                this.controller.process("Transaction-page-select", card);
            }
        });
    }

    private void hoverFadePayee(Node node, Account payee)
    {
        Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 0, new SineInterpolator())
                )
        );

        node.setOnMouseEntered(event ->
        {
            if (node != this.selectedPayeeNode) {
                hoverIn.playFromStart();
            }
        });

        node.setOnMouseExited(event ->
        {
            if (node != this.selectedPayeeNode) {
                hoverOut.playFromStart();
            }
        });

        node.setOnMouseClicked(event ->
        {
            if (node != this.selectedPayeeNode)
            {
                // Fade out the others
                for (AnchorPane payeeNode : allPayees)
                {
                    if (payeeNode != node)
                    {
                        Timeline fadeOutOthers = new Timeline(
                                new KeyFrame(Duration.seconds(0.2),
                                        new KeyValue(payeeNode.opacityProperty(), 0, new SineInterpolator()))
                        );
                        fadeOutOthers.playFromStart();
                    }
                }

                // Fade in the selected one
                Timeline fadeInSelected = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(node.opacityProperty(), 1, new SineInterpolator()))
                );

                fadeInSelected.playFromStart();

                if (this.selectedPayeeAccount != null)
                {
                    prevSelectedPayeeAccount = this.selectedPayeeAccount;
                }

                this.selectedPayeeNode = (AnchorPane) node;
                this.selectedPayeeAccount = payee;

                this.controller.process("Transaction-page-select", payee);
            }
        });
    }

    public void registerText(Text text)
    {
        hoverNode(text, 0.6, 1);

        text.setOnMouseClicked(event ->
        {
            controller.process(text.getId());
        });
    }

    public void registerAnchorpane(AnchorPane anchorpane)
    {
        hoverNode(anchorpane, 1, 0);

        anchorpane.setOnMouseClicked(event ->
        {
            System.out.println(anchorpane.getId());
            controller.process(anchorpane.getId());
        });
    }

    public void registerSort()
    {
        hoverNode(sortButton, 0.5, 1);

        sortButton.setOnMouseClicked(event ->
        {
            sortButton.setText(sorts[sortIndex]);

            sortIndex = (sortIndex + 1) % sorts.length;

            this.update();
        });
    }

    private void registerCard(AnchorPane anchorCard, Card card)
    {
        allCards.add(anchorCard);
        hoverFadeCards(anchorCard, card);
    }

    private void registerPayee(AnchorPane anchorPayee, Account payee)
    {
        allPayees.add(anchorPayee);
        hoverFadePayee(anchorPayee, payee);
    }

    private void registerTextField(TextField textField)
    {
        textField.setOnMouseClicked(event ->
        {
            controller.process(textField.getId());
        });
    }

    private void registerButton(Button button)
    {
        hoverNode(button, 1, 0);

        //TODO: Add ripple effect
        button.setOnMousePressed(event ->
        {
            this.controller.process(button.getId());
        });
    }

    private void slideTransaction(States transactionState)
    {

        double targetY = switch (transactionState)
        {
            case TRANSACTION_PAGE, TRANSACTION_CONFIRM, TRANSACTION_COMPLETE -> 72;
            default -> 896;
        };

        double targetYreview = switch (transactionState)
        {
            case TRANSACTION_CONFIRM, TRANSACTION_COMPLETE -> 72;
            default -> 896;
        };

        double targetNComplete = switch (transactionState)
        {
            case TRANSACTION_COMPLETE -> 1;
            default -> 0;
        };

        double targetYsummary = switch (transactionState)
        {
            case TRANSACTION_SUMMARY -> 72;
            default -> 896;
        };


        Timeline transactionCompleteAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(transactionCompleteScene.opacityProperty(), targetNComplete, new SineInterpolator())
                )
        );

        transactionCompleteScene.setMouseTransparent(transactionState != TRANSACTION_COMPLETE);

        transactionCompleteAnimation.playFromStart();

        Timeline transactionReviewAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(transactionReviewScene.translateYProperty(), targetYreview, new SineInterpolator())
                )
        );

        transactionReviewAnimation.playFromStart();

        if (!this.transactionState.equals(States.TRANSACTION_SUMMARY))
        {
            Timeline transactionSummaryAnimation = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(transactionSummaryScene.translateYProperty(), targetYsummary, new SineInterpolator())
                    )
            );

            transactionSummaryAnimation.playFromStart();
        }


        if (!this.transactionState.equals(States.FEATURES_PAGE))
        {
            double targetMenu = switch (transactionState)
            {
                case FEATURES_PAGE -> 72;
                default -> 896;
            };

            Timeline menuAnimation = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(featureMenuScene.translateYProperty(), targetMenu, new SineInterpolator())
                    )
            );

            menuAnimation.playFromStart();
        }

        Timeline transactionAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(transactionScene.translateYProperty(), targetY, new SineInterpolator())
                )
        );

        transactionAnimation.playFromStart();

        this.transactionState = transactionState;
    }

    public void slide(States state)
    {
        if (this.accountState.equals(state))
        {
            return;
        }

        this.accountState = state;

        double targetAnchor = switch (state)
        {
            case HOME_PAGE, WALLET_PAGE, PROFILE_PAGE -> 0;
            default -> 464;
        };

        double targetHbox = switch (state)
        {
            case WALLET_PAGE -> -424;
            case PROFILE_PAGE -> -424 * 2;
            default -> 0;
        };

        anchor.setMouseTransparent(state == States.DEFAULT);

        Timeline anchorAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), targetAnchor, new SineInterpolator())
                )
        );

        Timeline hboxAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchorHbox.translateXProperty(), targetHbox, new SineInterpolator())
                )
        );

        hboxAnimation.play();
        anchorAnimation.play();
    }

    private void updateTransactionSummary(Account account)
    {
        List<History> historyList = account.getHistory();

        Card currentCard = account.getCard();

        transactionContainer1.getChildren().clear();

        String lastDate = null;

        // using java's stream to manipulate the history list sequentially with filter and mapping.
        double total = historyList.stream().
                filter(history -> history.card().equals(currentCard)).
                mapToDouble(History::amount).
                sum();

        List<History> filteredhistoryList = switch (this.sortIndex)
        {
            // Including all transactions.
            case 0 -> historyList.stream().
                    filter(history -> history.card().equals(currentCard)).
                    toList();
            // Income transactions only.
            case 1 -> historyList.stream().
                    filter(history -> history.card().equals(currentCard)).
                    filter(value -> value.amount() > 0).
                    toList();
            // Expense transactions only.
            case 2 -> historyList.stream().
                    filter(history -> history.card().equals(currentCard)).
                    filter(value -> value.amount() < 0).
                    toList();
            default -> throw new IllegalStateException("Unexpected value: " + this.sortIndex);
        };

        for (History history : filteredhistoryList)
        {
            String currentDate = history.date();
            // If the date is different from the last date, add a date header.
            if (!currentDate.equals(lastDate))
            {
                AnchorPane dateHeader = buildTransactionDate(currentDate, total, transactionDate);
                transactionContainer1.getChildren().add(dateHeader);
                lastDate = currentDate;
            }

            AnchorPane transaction = buildTransactionPane(
                    history.payee(),
                    history.date(),
                    history.time(),
                    history.amount(),
                    transactionItem
            );

            transactionContainer1.getChildren().add(transaction);
        }
    }

    private void updateTransactionConfirmation(Account account)
    {
        double inputBalance = this.model.getInputBalance();
        Card currentCard = this.selectedCard;

        String username = account.getName();

        this.payerUsername.setText(username);

        double balance = currentCard.getCardBalance();

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        String formattedBalance = formatter.format(balance);

        this.payerBalance.setText(formattedBalance);

        String sort = currentCard.getCardSort();
        String number = currentCard.getCardNumber();

        this.payerPIN.setText(sort + " " + number);

        Types type = account.getType();

        this.payerType.setText(type + " Account");

        String companyName = currentCard.getCardCompany();
        Types cardType = currentCard.getCardType();

        this.payerCompany.setText(cardType + " " + companyName);
        this.payerCompanyIcon.setImage(View.getCardImage(companyName));

        String formattedInputBalance = formatter.format(inputBalance);

        this.reviewAmount.setText(formattedInputBalance);

        // Update the payee's preview card
        if (this.selectedPayeeAccount != null)
        {
            Account payeeAccount = this.selectedPayeeAccount;
            Card payeeCard = payeeAccount.getCard();

            this.payeeUsername.setText(payeeAccount.getName());

            int payeeNumberLength = payeeCard.getCardNumber().length();
            String payeeNumber = payeeCard.getCardNumber().substring(payeeNumberLength - 3);

            this.payeePIN.setText("**-**-** ***" + payeeNumber);

            this.payeeType.setText(payeeAccount.getType() + " Account");

            String payeeCompanyName = payeeCard.getCardCompany();
            Types payeeCardType = payeeCard.getCardType();

            this.payeeCompany.setText(payeeCardType + " " + payeeCompanyName);
            this.payeeCompanyIcon.setImage(View.getCardImage(payeeCompanyName));
        }
    }

    private void updateCardHolder(Account account)
    {
        // Method for updating the cardholder's card from the homepage

        Card currentCard = account.getCard();

        System.out.println(currentCard);

        // Fetching the current account's name, and extracting into first or last name.
        String username = account.getName();
        String firstName = username.split(" ")[0];

        this.username.setText(username);
        this.firstName.setText(firstName);

        double balance = currentCard.getCardBalance();

        // Using javafx number formatter that converts numbers into formated and parsing strings.
        // Format the balance with initial £, comma separators for each group, and two decimal places as pences.
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        String formattedBalance = formatter.format(balance);

        this.accountBalance.setText(formattedBalance);

        String sort = currentCard.getCardSort();
        String number = currentCard.getCardNumber();

        this.accountPIN.setText(sort + " " + number);

        Types type = account.getType();

        this.accountType.setText(type + " Account");

        String companyName = currentCard.getCardCompany();
        Types cardType = currentCard.getCardType();

        this.accountCompany.setText(cardType + " " + companyName);
        this.accountCompanyIcon.setImage(View.getCardImage(companyName));

        List<History> historyList = account.getHistory();

        transactionContainer.getChildren().clear();

        String lastDate = null;
        double sum = historyList.stream().
                mapToDouble(History::amount).
                sum();

        for (History history : historyList)
        {
            if (!history.card().equals(currentCard))
            {
                break;
            }

            String currentDate = history.date();
            // If the date is different from the last date, add a date header.
            if (!currentDate.equals(lastDate))
            {
                AnchorPane dateHeader = buildTransactionDate(currentDate, sum, transactionDate);
                transactionContainer.getChildren().add(dateHeader);
                lastDate = currentDate;
            }

            AnchorPane transaction = buildTransactionPane(
                    history.payee(),
                    history.date(),
                    history.time(),
                    history.amount(),
                    transactionItem
            );

            transactionContainer.getChildren().add(transaction);
        }
    }

    private void updateTransaction(Account account)
    {
        States inputState = this.model.getState(Scene.INPUT);

        long input = this.model.getInput();
        double balance = this.model.getInputBalance();

        if (inputState.equals(States.INPUT_NUMERIC))
        {
            String stringInput = String.valueOf(input);

            if (!stringInput.equals("0"))
            {
                this.recipientTextField.setText(stringInput);
            }
            else
            {
                this.recipientTextField.setText("Enter 6 digit PIN code");
            }

            // Since each input automatically updates this method.
            // we must track the current styles before removing or applying them

            ObservableList<String> styleClasses = this.recipientTextField.getStyleClass();

            // Assume that the styleClass of the recipient contains more than 3 styles.
            if (!styleClasses.isEmpty())
            {
                // Assume that the last style applied should be the one to be replaced
                // with another style.

                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                // Assume the last style is it's base style
                if (lastStyle.equals("baseTextField"))
                {
                    styleClasses.set(lastIndex, "baseTextField-pinpoint");
                }
            }
        }
        else
        {
            // Same to the previous technique, except we revert the style of the textfield.
            ObservableList<String> styleClasses = this.recipientTextField.getStyleClass();

            if (!styleClasses.isEmpty())
            {
                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                if (lastStyle.equals("baseTextField-pinpoint"))
                {
                    styleClasses.set(lastIndex, "baseTextField");
                }
            }
        }

        if (inputState.equals(States.INPUT_BALANCE))
        {
            String stringBalance = String.format("%.2f", balance);

            if (!stringBalance.equals("0.00"))
            {
                this.amountTextField.setText("£" + stringBalance);
            }
            else
            {
                this.amountTextField.setText("Enter the amount");
            }

            ObservableList<String> styleClasses = this.amountTextField.getStyleClass();

            if (!styleClasses.isEmpty())
            {
                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                if (lastStyle.equals("baseTextField"))
                {
                    styleClasses.set(lastIndex, "baseTextField-pinpoint");
                }
            }
        }
        else
        {
            ObservableList<String> styleClasses = this.amountTextField.getStyleClass();

            if (!styleClasses.isEmpty())
            {
                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                if (lastStyle.equals("baseTextField-pinpoint"))
                {
                    styleClasses.set(lastIndex, "baseTextField");
                }
            }
        }

        if (this.selectedCard != null)
        {
            this.textSelectedCard.setText(this.selectedCard.getCardSort() + " " +  this.selectedCard.getCardNumber());
            this.textSelectedBalance.setText("£" +  this.selectedCard.getCardBalance());
        }
        else
        {
            this.textSelectedCard.setText("None");
            this.textSelectedBalance.setText("£0.00");
        }

        if (this.selectedPayeeAccount != null)
        {
            this.textSelectedPayee.setText(this.selectedPayeeAccount.getName());
        }
        else
        {
            this.textSelectedPayee.setText("No one");
        }

        this.payeesContainer.getChildren().clear();
        this.allPayees.clear();

        ArrayList<Account> accounts = this.model.findAccountPartially(input);
        boolean isSelectedSeen = false;

        for (Account payee : accounts)
        {
            // Skip for the next iteration, since the owner isn't supposed to see their account from the search.
            if (account.getNumber() == payee.getNumber())
            {
                continue;
            }

            AnchorPane payeeItemContainer = new AnchorPane();
            payeeItemContainer.getStyleClass().addAll(payeeItemContainer.getStyleClass());
            payeeItemContainer.setPrefSize(payeeItemContainer.getPrefWidth(), payeeItemContainer.getPrefHeight());

            AnchorPane bluePayee = buildPayeePane(payee, payeeItemBlue, false);
            AnchorPane greyPayee = buildPayeePane(payee, payeeItemGrey, true);

            bluePayee.setOpacity(0);

            boolean isSelected =  this.selectedPayeeAccount != null && this.selectedPayeeAccount.equals(payee);
            boolean isPrevSelected = this.prevSelectedPayeeAccount != null && this.prevSelectedPayeeAccount.equals(payee);

            if (isSelected)
            {
                isSelectedSeen = true;
                bluePayee.setOpacity(1);
                this.selectedPayeeNode = bluePayee; // rebind the node to this fresh one
                this.selectedPayeeAccount = payee;
            }

            if (isPrevSelected)
            {
                this.prevSelectedPayeeAccount = null;
                bluePayee.setOpacity(1);
                Timeline fadeOutOthers = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(bluePayee.opacityProperty(), 0, new SineInterpolator()))
                );
                fadeOutOthers.playFromStart();
            }

            payeeItemContainer.getChildren().addAll(greyPayee, bluePayee);

            registerPayee(bluePayee, payee);

            this.payeesContainer.getChildren().add(payeeItemContainer);
        }

        if (!isSelectedSeen || accounts.isEmpty())
        {
            this.selectedPayeeNode = null;
            this.selectedPayeeAccount = null;
            this.prevSelectedPayeeAccount = null;
        }

        this.cardContainer.getChildren().clear();
        this.allCards.clear();

        List<Card> cards = account.getCards();

        // Javafx doesn't support cloning, which I had to do manual duplication instead.
        // by using two different helper methods.
        for (Card card : cards)
        {
            AnchorPane cardItemContainer = new AnchorPane();
            cardItemContainer.getStyleClass().addAll(cardItemContainer.getStyleClass());
            cardItemContainer.setPrefSize(cardItemContainer.getPrefWidth(), cardItemContainer.getPrefHeight());

            AnchorPane blueCard = buildCardPane(card, cardItemBlue, false);
            AnchorPane greyCard = buildCardPane(card, cardItemGrey, true);

            blueCard.setOpacity(0);

            boolean isSelected = this.selectedCard != null && Objects.equals(this.selectedCard.getCardNumber(), card.getCardNumber());
            boolean isPrevSelected = this.prevSelectedCard != null && Objects.equals(this.prevSelectedCard.getCardNumber(), card.getCardNumber());

            if (isSelected)
            {
                blueCard.setOpacity(1);
                this.selectedCardNode = blueCard; // rebind the node to this fresh one
                this.selectedCard = card;
            }

            if (isPrevSelected)
            {
                this.prevSelectedCard = null;
                blueCard.setOpacity(1);
                Timeline fadeOutOthers = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(blueCard.opacityProperty(), 0, new SineInterpolator()))
                );
                fadeOutOthers.playFromStart();
            }

            cardItemContainer.getChildren().addAll(greyCard, blueCard);

            registerCard(blueCard, card);

            this.cardContainer.getChildren().add(cardItemContainer);
        }

        if (cards.isEmpty())
        {
            this.selectedCardNode = null;
            this.selectedCard = null;
            this.prevSelectedCard = null;
        }
    }

    public void update()
    {
        Account account = this.model.getAccount();

        if (account != null)
        {
            States accountState = this.model.getState(Scene.ACCOUNT);
            States transactionState = this.model.getState(Scene.TRANSACTION);

            slideTransaction(transactionState);

            // Only update these elements necessarily based on the scene shown.
            if (accountState.equals(States.HOME_PAGE))
            {
                updateCardHolder(account);
            }

            if (transactionState.equals(States.TRANSACTION_PAGE))
            {
                updateTransaction(account);
            }

            if (transactionState.equals(States.TRANSACTION_CONFIRM))
            {
                updateTransactionConfirmation(account);
            }

            if (transactionState.equals(States.TRANSACTION_SUMMARY))
            {
                updateTransactionSummary(account);
            }
            else if (transactionState.equals(DEFAULT))
            {
                    this.recipientTextField.setText("Enter 6 digit PIN code");
                    this.amountTextField.setText("Enter the amount");

                    this.selectedCardNode = null;
                    this.selectedCard = null;
                    this.prevSelectedCard = null;
                    this.allCards.clear();

                    this.selectedPayeeNode = null;
                    this.selectedPayeeAccount = null;
                    this.prevSelectedPayeeAccount = null;
                    this.allPayees.clear();
            }

            System.out.println(transactionState);
        }
    }

    // Node cloning helper
    private Node cloneNode(Node node)
    {
        Node copy = null;

        if (node instanceof Text original)
        {
            Text text = new Text(original.getText());
            text.setId(original.getId());
            text.setLayoutX(original.getLayoutX());
            text.setLayoutY(original.getLayoutY());
            text.setOpacity(original.getOpacity());
            text.setTextAlignment(original.getTextAlignment());
            text.getStyleClass().addAll(original.getStyleClass());
            copy = text;
        }
        else if (node instanceof AnchorPane original)
        {
            AnchorPane pane = new AnchorPane();
            pane.setId(original.getId());
            pane.setPrefHeight(original.getPrefHeight());
            pane.setPrefWidth(original.getPrefWidth());
            pane.setLayoutX(original.getLayoutX());
            pane.setLayoutY(original.getLayoutY());
            pane.getStyleClass().addAll(original.getStyleClass());
            pane.setPadding(original.getPadding());
            copy = pane;
        } else if (node instanceof ImageView original) {
            ImageView image = new ImageView();
            image.setId(original.getId());
            image.setFitHeight(original.getFitHeight());
            image.setFitWidth(original.getFitWidth());
            image.setLayoutX(original.getLayoutX());
            image.setLayoutY(original.getLayoutY());
            image.setPreserveRatio(original.isPreserveRatio());
            image.getStyleClass().addAll(original.getStyleClass());
            copy = image;
        } else if (node instanceof Circle original) {
            Circle circle = new Circle();
            circle.setId(original.getId());
            circle.setRadius(original.getRadius());
            circle.setLayoutX(original.getLayoutX());
            circle.setLayoutY(original.getLayoutY());
            circle.getStyleClass().addAll(original.getStyleClass());
            copy = circle;
        } else if (node instanceof Button original)
        {
            Button button = new Button();
            button.setId(original.getId());
            button.setPrefWidth(original.getPrefWidth());
            button.setLayoutX(original.getLayoutX());
            button.setLayoutY(original.getLayoutY());
            button.getStyleClass().addAll(original.getStyleClass());
            copy = button;
        }
        else if (node instanceof VBox original)
        {
            VBox vbox = new VBox();
            vbox.setId(original.getId());
            vbox.setPrefWidth(original.getPrefWidth());
            vbox.setPrefHeight(original.getPrefHeight());
            vbox.setLayoutX(original.getLayoutX());
            vbox.setLayoutY(original.getLayoutY());
            vbox.setAlignment(original.getAlignment());
            vbox.setPadding(original.getPadding());
            vbox.getStyleClass().addAll(original.getStyleClass());

            if (original.getChildren() != null)
            {
                for (Node child : original.getChildren())
                {
                    vbox.getChildren().add(cloneNode(child));
                }
            }

            copy = vbox;
        }
        else if (node instanceof HBox original)
        {
            HBox hbox = new HBox();
            hbox.setId(original.getId());
            hbox.setPrefWidth(original.getPrefWidth());
            hbox.setPrefHeight(original.getPrefHeight());
            hbox.setLayoutX(original.getLayoutX());
            hbox.setLayoutY(original.getLayoutY());
            hbox.setAlignment(original.getAlignment());
            hbox.setPadding(original.getPadding());
            hbox.getStyleClass().addAll(original.getStyleClass());

            if (original.getChildren() != null)
            {
                for (Node child : original.getChildren())
                {
                    hbox.getChildren().add(cloneNode(child));
                }
            }

            copy = hbox;
        }
        else if (node instanceof BorderPane original)
        {
            BorderPane border = new BorderPane();
            border.setId(original.getId());
            border.setPrefWidth(original.getPrefWidth());
            border.setPrefHeight(original.getPrefHeight());
            border.setLayoutX(original.getLayoutX());
            border.setLayoutY(original.getLayoutY());
            border.getStyleClass().addAll(original.getStyleClass());
            border.setPadding(original.getPadding());

            if (original.getTop() != null)
                border.setTop(cloneNode(original.getTop()));
            if (original.getBottom() != null)
                border.setBottom(cloneNode(original.getBottom()));
            if (original.getLeft() != null)
                border.setLeft(cloneNode(original.getLeft()));
            if (original.getRight() != null)
                border.setRight(cloneNode(original.getRight()));
            if (original.getCenter() != null)
                border.setCenter(cloneNode(original.getCenter()));

            copy = border;
        }

        // Try copying margin if the parent exists and the parent uses it.
        if (copy != null && node.getParent() != null)
        {
            Insets margin = null;

            if (node.getParent() instanceof VBox)
            {
                margin = VBox.getMargin(node);
                if (margin != null) VBox.setMargin(copy, margin);
            }
            else if (node.getParent() instanceof HBox)
            {
                margin = HBox.getMargin(node);
                if (margin != null) HBox.setMargin(copy, margin);
            }
        }

        return (copy != null) ? copy : new Group();
    }

    private AnchorPane buildTransactionPane(Account payee, String date, String time, double amount, AnchorPane template)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        int majorUnit = (int) amount;
        int minorUnit = (int) Math.round((amount - majorUnit) * 100);

        for (Node instance : template.getChildren())
        {
            Node copy = cloneNode(instance);

            switch (copy)
            {
                // assume that the payee container has their name, date, time, and amount.
                case Text text -> {
                    if (text.getId().equals("transactionName"))
                    {
                        text.setText(payee.getName());
                    }
                    if (text.getId().equals("transactionDate"))
                    {
                        text.setText(date);
                    }
                    if (text.getId().equals("transactionTime"))
                    {
                        text.setText(time);
                    }
                }
                case HBox hBox ->
                {
                    for (Node child : hBox.getChildren())
                    {
                        if (child instanceof Text text)
                        {
                            if (text.getId().equals("transactionMajorUnit"))
                            {
                                DecimalFormat formatter = new DecimalFormat("#,###");

                                String formattedMajor = formatter.format(majorUnit);

                                text.setText("+ £" + formattedMajor);
                            }
                            if (text.getId().equals("transactionMinorUnit"))
                            {
                                String formattedMinor = "." + String.format("%02d", minorUnit);

                                text.setText(formattedMinor);
                            }
                        }
                    }
                }
                case ImageView imageView when imageView.getId().equals("transactionIcon") ->
                {
                    String icon = "profile";
                    imageView.setImage(View.getCardImage(icon));
                }
                default -> {}
            }

            pane.getChildren().add(copy);
        }

        return pane;
    }

    private AnchorPane buildTransactionDate(String date, double total, AnchorPane template)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        int majorUnit = (int) total; // whole part (e.g., pounds)
        int minorUnit = (int) Math.round((total - majorUnit) * 100); // fractional part (e.g., pence)

        for (Node instance : template.getChildren())
        {
            Node copy = cloneNode(instance);

            if (copy instanceof BorderPane borderPane)
            {
                Node leftNode = borderPane.getLeft();
                Node rightNode = borderPane.getRight();

                // assume that the rightNode is the date of the transaction
                // (added HBox container since the alignment behavior doesn't copy if parented with the borderPane)

                if (leftNode instanceof HBox hbox)
                {
                    for (Node child : hbox.getChildren())
                    {
                        if (child instanceof Text text)
                        {
                            if (text.getId().equals("transactionDate"))
                            {
                                text.setText(date);
                            }
                        }
                    }
                }

                // assume that the rightNode is a container that has both major and minor units.
                if (rightNode instanceof HBox hbox)
                {
                    for (Node child : hbox.getChildren())
                    {
                        if (child instanceof Text text)
                        {
                            if (text.getId().equals("transactionMajorUnit"))
                            {
                                DecimalFormat formatter = new DecimalFormat("#,###");

                                String formattedMajor = formatter.format(majorUnit);

                                // major unit (e.g., "£1,234")
                                text.setText("+ £" + formattedMajor);
                            }
                            if (text.getId().equals("transactionMinorUnit"))
                            {
                                // Always show 2 digits (e.g., "07", "89")
                                String formattedMinor = "." + String.format("%02d", minorUnit);

                                // minor unit (e.g., ".99" pence)
                                text.setText(formattedMinor);
                            }
                        }
                    }
                };
            }


            pane.getChildren().add(copy);
        }

        return pane;
    }

    private AnchorPane buildCardPane(Card card, AnchorPane template, boolean isGrey)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        for (Node instance : template.getChildren())
        {
            Node copy = cloneNode(instance);

            switch (copy)
            {
                case Text text -> {
                    if (text.getId().equals("cardName"))
                    {
                        text.setText(card.getCardType() + " " + card.getCardCompany());
                    }
                    if (text.getId().equals("cardNumber"))
                    {
                        text.setText(card.getCardSort() + " " + card.getCardNumber());
                    }
                }
                case ImageView imageView when imageView.getId().equals("cardIcon") ->
                {
                    String icon = isGrey ? card.getCardCompany() + "-grey" : card.getCardCompany();
                    imageView.setImage(View.getCardImage(icon));
                }
                default -> {}
            }

            pane.getChildren().add(copy);
        }

        return pane;
    }

    private AnchorPane buildPayeePane(Account payee, AnchorPane template, boolean isGrey)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        for (Node instance : template.getChildren())
        {
            Node copy = cloneNode(instance);

            switch (copy)
            {
                case VBox vbox ->
                {
                    for (Node VBoxChild : vbox.getChildren())
                    {
                        if (VBoxChild instanceof Text text)
                        {
                            if (text.getId().equals("payeeName"))
                            {
                                String firstName = payee.getName().split(" ")[0];
                                text.setText(firstName);
                            }
                        }
                    }
                }
                case ImageView imageView ->
                {
                    if (imageView.getId().equals("payeeIcon"))
                    {
                        String icon = isGrey ? "profile-grey" : "profile";
                        imageView.setImage(View.getCardImage(icon));
                    }
                }
                default -> {}
            }

            pane.getChildren().add(copy);
        }

        return pane;
    }
}
