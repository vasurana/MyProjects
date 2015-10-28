from django.db import models

# Create your models here.
class profile(models.Model):
	name = models.CharField(max_length = 1200)
	description = models.TextField(default = 'description default')
	
	def __unicode__(self):
		return self.name

class fir(models.Model):
	name = models.CharField(max_length= 1200)
	uin = models.CharField(max_length =1200)
	contact = models.CharField(max_length = 1200)
	address = models.TextField(default = 'description default')
	aname = models.CharField(max_length = 1200)
	description = models.TextField(default = 'description default')
	"""docstring for fir"""
	def __unicode__(self):
		return self.name
		
		