#include "lighting.h"

#include <QtWidgets>
#include <QtCore/qmath.h>

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

Lighting::Lighting(QWidget *parent): QGraphicsView(parent), angle(0.0)
{
    setScene(&m_scene);

    setupScene();

    QTimer *timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), SLOT(animate()));
    timer->setInterval(30);
    timer->start();

    setRenderHint(QPainter::Antialiasing, true);
    setFrameStyle(QFrame::NoFrame);
}

void Lighting::setupScene()
{
    m_scene.setSceneRect(-300, -200, 600, 460);

    QLinearGradient linearGrad(QPointF(-100, -100), QPointF(100, 100));
    linearGrad.setColorAt(0, QColor(255, 255, 255));
    linearGrad.setColorAt(1, QColor(192, 192, 255));
    setBackgroundBrush(linearGrad);

    QRadialGradient radialGrad(30, 30, 30);
    radialGrad.setColorAt(0, Qt::yellow);
    radialGrad.setColorAt(0.2, Qt::yellow);
    radialGrad.setColorAt(1, Qt::transparent);
    QPixmap pixmap(60, 60);
    pixmap.fill(Qt::transparent);
    QPainter painter(&pixmap);
    painter.setPen(Qt::NoPen);
    painter.setBrush(radialGrad);
    painter.drawEllipse(0, 0, 60, 60);
    painter.end();

    m_lightSource = m_scene.addPixmap(pixmap);
    m_lightSource->setZValue(2);

    for (int i = -2; i < 3; ++i)
        for (int j = -2; j < 3; ++j) {
            QAbstractGraphicsShapeItem *item;
            if ((i + j) & 1)
                item = new QGraphicsEllipseItem(0, 0, 50, 50);
            else
                item = new QGraphicsRectItem(0, 0, 50, 50);

            item->setPen(QPen(Qt::black, 1));
            item->setBrush(QBrush(Qt::white));
            QGraphicsDropShadowEffect *effect = new QGraphicsDropShadowEffect;
            effect->setBlurRadius(8);
            item->setGraphicsEffect(effect);
            item->setZValue(1);
            item->setPos(i * 80, j * 80);
            m_scene.addItem(item);
            m_items << item;
        }

}

void Lighting::animate()
{
    angle += (M_PI / 30);
    qreal xs = 200 * qSin(angle) - 40 + 25;
    qreal ys = 200 * qCos(angle) - 40 + 25;
    m_lightSource->setPos(xs, ys);

    for (int i = 0; i < m_items.size(); ++i) {
        QGraphicsItem *item = m_items.at(i);
        Q_ASSERT(item);
        QGraphicsDropShadowEffect *effect = static_cast<QGraphicsDropShadowEffect *>(item->graphicsEffect());
        Q_ASSERT(effect);

        QPointF delta(item->x() - xs, item->y() - ys);
        effect->setOffset(delta.toPoint() / 30);

        qreal dx = delta.x();
        qreal dy = delta.y();
        qreal dd = qSqrt(dx * dx + dy * dy);
        QColor color = effect->color();
        color.setAlphaF(qBound(0.4, 1 - dd / 200.0, 0.7));
        effect->setColor(color);
    }

    m_scene.update();
}

void Lighting::resizeEvent(QResizeEvent * /* event */)
{
}