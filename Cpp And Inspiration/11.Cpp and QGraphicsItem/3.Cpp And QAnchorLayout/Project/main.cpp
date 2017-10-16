#include <QtWidgets>

static QGraphicsProxyWidget* newQGraphicsProxyWidget(QSizeF minSize, QSizeF prefSize, QSizeF maxSize, QString name)
{
	QGraphicsProxyWidget* proxyWidget=new QGraphicsProxyWidget();
	proxyWidget->setWidget(new QPushButton(name));
	proxyWidget->setData(0, name);
	proxyWidget->setMinimumSize(minSize);
	proxyWidget->setPreferredSize(prefSize);
	proxyWidget->setMaximumSize(maxSize);
	proxyWidget->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Preferred);
	return proxyWidget;
}

int main(int argc, char** argv)
{
	QApplication application(argc, argv);
	QGraphicsScene scene(0, 0, 800, 480);

	QSizeF minSize(30, 100);
	QSizeF prefSize(210, 100);
	QSizeF maxSize(300, 100);

	QGraphicsProxyWidget* a = newQGraphicsProxyWidget(minSize, prefSize, maxSize, "A");
	QGraphicsProxyWidget* b = newQGraphicsProxyWidget(minSize, prefSize, maxSize, "B");
	QGraphicsProxyWidget* c = newQGraphicsProxyWidget(minSize, prefSize, maxSize, "C");
	QGraphicsProxyWidget* d = newQGraphicsProxyWidget(minSize, prefSize, maxSize, "D");
	QGraphicsProxyWidget* e = newQGraphicsProxyWidget(minSize, prefSize, maxSize, "E");
	QGraphicsProxyWidget* f = newQGraphicsProxyWidget(QSizeF(30, 50), QSizeF(150, 50), maxSize, "F (overflow)");
	QGraphicsProxyWidget* g = newQGraphicsProxyWidget(QSizeF(30, 50), QSizeF(30, 100), maxSize, "G (overflow)");

	QGraphicsAnchorLayout* anchorLayout= new QGraphicsAnchorLayout();
	anchorLayout->setSpacing(0);

	QGraphicsWidget* graphicsWidget = new QGraphicsWidget(0, Qt::Window);
	graphicsWidget->setPos(20, 20);
	graphicsWidget->setLayout(anchorLayout);

	anchorLayout->addAnchor(a, Qt::AnchorTop, anchorLayout, Qt::AnchorTop);
	anchorLayout->addAnchor(b, Qt::AnchorTop, anchorLayout, Qt::AnchorTop);

	anchorLayout->addAnchor(c, Qt::AnchorTop, a, Qt::AnchorBottom);
	anchorLayout->addAnchor(c, Qt::AnchorTop, b, Qt::AnchorBottom);
	anchorLayout->addAnchor(c, Qt::AnchorBottom, d, Qt::AnchorTop);
	anchorLayout->addAnchor(c, Qt::AnchorBottom, e, Qt::AnchorTop);

	anchorLayout->addAnchor(d, Qt::AnchorBottom, anchorLayout, Qt::AnchorBottom);
	anchorLayout->addAnchor(e, Qt::AnchorBottom, anchorLayout, Qt::AnchorBottom);

	anchorLayout->addAnchor(c, Qt::AnchorTop, f, Qt::AnchorTop);
	anchorLayout->addAnchor(c, Qt::AnchorVerticalCenter, f, Qt::AnchorBottom);
	anchorLayout->addAnchor(f, Qt::AnchorBottom, g, Qt::AnchorTop);
	anchorLayout->addAnchor(c, Qt::AnchorBottom, g, Qt::AnchorBottom);

	anchorLayout->addAnchor(anchorLayout, Qt::AnchorLeft, a, Qt::AnchorLeft);
	anchorLayout->addAnchor(anchorLayout, Qt::AnchorLeft, d, Qt::AnchorLeft);
	anchorLayout->addAnchor(a, Qt::AnchorRight, b, Qt::AnchorLeft);

	anchorLayout->addAnchor(a, Qt::AnchorRight, c, Qt::AnchorLeft);
	anchorLayout->addAnchor(c, Qt::AnchorRight, e, Qt::AnchorLeft);

	anchorLayout->addAnchor(b, Qt::AnchorRight, anchorLayout, Qt::AnchorRight);
	anchorLayout->addAnchor(e, Qt::AnchorRight, anchorLayout, Qt::AnchorRight);
	anchorLayout->addAnchor(d, Qt::AnchorRight, e, Qt::AnchorLeft);

	anchorLayout->addAnchor(anchorLayout, Qt::AnchorLeft, f, Qt::AnchorLeft);
	anchorLayout->addAnchor(anchorLayout, Qt::AnchorLeft, g, Qt::AnchorLeft);
	anchorLayout->addAnchor(f, Qt::AnchorRight, g, Qt::AnchorRight);

	scene.addItem(graphicsWidget);
	scene.setBackgroundBrush(Qt::darkGreen);
	QGraphicsView graphicsView(&scene);
	graphicsView.show();
	return application.exec();
}