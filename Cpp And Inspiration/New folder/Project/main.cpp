#include "lighting.h"
#include <QApplication>

int main(int argc, char **argv)
{
    QApplication app(argc, argv);

    Lighting lighting;
    lighting.setWindowTitle(QT_TRANSLATE_NOOP(QGraphicsView, "Lighting and Shadows"));

    lighting.resize(640, 480);
    lighting.show();

    return app.exec();
}