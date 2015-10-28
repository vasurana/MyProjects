# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('profiles', '0010_auto_20150430_1513'),
    ]

    operations = [
        migrations.CreateModel(
            name='fir',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=1200)),
                ('uin', models.CharField(max_length=1200)),
                ('contact', models.CharField(max_length=1200)),
                ('address', models.TextField(default=b'description default')),
                ('aname', models.CharField(max_length=1200)),
                ('description', models.TextField(default=b'description default')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
