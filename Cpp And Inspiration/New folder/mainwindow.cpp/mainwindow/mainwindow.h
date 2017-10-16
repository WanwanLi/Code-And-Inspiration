#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

class ToolBar;
QT_FORWARD_DECLARE_CLASS(QMenu)

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    typedef QMap<QString, QSize> CustomSizeHintMap;

    explicit MainWindow(const CustomSizeHintMap &customSizeHints,
                        QWidget *parent = Q_NULLPTR,
                        Qt::WindowFlags flags = 0);

public slots:
    void actionTriggered(QAction *action);
    void saveLayout();
    void loadLayout();
    void switchLayoutDirection();
    void setDockOptions();

    void createDockWidget();
    void destroyDockWidget(QAction *action);

private:
    void setupToolBar();
    void setupMenuBar();
    void setupDockWidgets(const CustomSizeHintMap &customSizeHints);

    QList<ToolBar*> toolBars;
    QMenu *dockWidgetMenu;
    QMenu *mainWindowMenu;
    QList<QDockWidget *> extraDockWidgets;
    QMenu *destroyDockWidgetMenu;
};

#endif // MAINWINDOW_H