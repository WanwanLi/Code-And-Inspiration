#ifndef KNOB_H
 #define KNOB_H

 #include <QGraphicsItem>

 class Knob : public QGraphicsEllipseItem
 {
 public:
     Knob();

     bool sceneEvent(QEvent *event);
 };

 #endif // KNOB_H