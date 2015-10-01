# -*- coding: utf-8 -*-
import string
import re
import operator

#cipher text from the first problem
ciphertext1 = """Luama cgb jwluhjd bw PAMY mafgmrgeoa hj lugl; jwm ihi Gohka luhjr hl bw PAMY fqku wql ws lua cgy lw uagm lua Mgeehl bgy lw hlbaos, Wu iagm! Wu iagm! H bugoo ea ogla! (cuaj bua luwqdul hl wpam gslamcgmib, hl wkkqmmai lw uam lugl bua wqdul lw ugpa cwjiamai gl luhb, eql gl lua lhfa hl goo baafai vqhla jglqmgo); eql cuaj lua Mgeehl gklqgooy LWWR G CGLKU WQL WS HLB CGHBLKWGL-NWKRAL, gji owwrai gl hl, gji luaj uqmmhai wj, Gohka blgmlai lw uam saal, swm hl sogbuai gkmwbb uam fhji lugl bua ugi japam easwma baaj g mgeehl chlu ahluam g cghblkwgl nwkral, wm g cglku lw lgra wql ws hl, gji eqmjhjd chlu kqmhwbhly, bua mgj gkmwbb lua shaoi gslam hl, gji swmlqjglaoy cgb tqbl hj lhfa lw baa hl nwn iwcj g ogmda mgeehl-uwoa qjiam lua uaida. Hj gjwluam fwfajl iwcj cajl Gohka gslam hl, japam wjka kwjbhiamhjd uwc hj lua cwmoi bua cgb lw dal wql gdghj. Lua mgeehl-uwoa cajl blmghdul wj ohra g lqjjao swm bwfa cgy, gji luaj ihnnai bqiiajoy iwcj, bw bqiiajoy lugl Gohka ugi jwl g fwfajl lw luhjr gewql blwnnhjd uambaos easwma bua swqji uambaos sgoohjd iwcj g pamy iaan caoo. Ahluam lua caoo cgb pamy iaan, wm bua saoo pamy bowcoy, swm bua ugi noajly ws lhfa gb bua cajl iwcj lw owwr gewql uam gji lw cwjiam cugl cgb dwhjd lw ugnnaj jazl. Shmbl, bua lmhai lw owwr iwcj gji fgra wql cugl bua cgb kwfhjd lw, eql hl cgb lww igmr lw baa gjyluhjd; luaj bua owwrai gl lua bhiab ws lua caoo, gji jwlhkai lugl luay cama shooai chlu kqnewgmib gji ewwr- buaopab; uama gji luama bua bgc fgnb gji nhklqmab uqjd qnwj nadb. Bua lwwr iwcj g tgm smwf wja ws lua buaopab gb bua ngbbai; hl cgb ogeaooai WMGJDA FGMFGOGIA , eql lw uam dmagl ihbgnnwhjlfajl hl cgb afnly: bua ihi jwl ohra lw imwn lua tgm swm sagm ws rhoohjd bwfaewiy, bw fgjgdai lw nql hl hjlw wja ws lua kqnewgmib gb bua saoo ngbl hl. Caoo! luwqdul Gohka lw uambaos, gslam bqku g sgoo gb luhb, H bugoo luhjr jwluhjd ws lqfeohjd iwcj blghmb! Uwc emgpa luay oo goo luhjr fa gl uwfa! Cuy, H cwqoij l bgy gjyluhjd gewql hl, apaj hs H saoo wss lua lwn ws lua uwqba! (Cuhku cgb pamy ohraoy lmqa.) Iwcj, iwcj, iwcj. Cwqoi lua sgoo JAPAM kwfa lw gj aji! H cwjiam uwc fgjy fhoab H pa sgooaj ey luhb lhfa? bua bghi gowqi. H fqbl ea dallhjd bwfacuama jagm lua kajlma ws lua agmlu. Oal fa baa: lugl cwqoi ea swqm luwqbgji fhoab iwcj, H luhjr-- (swm, ywq baa, Gohka ugi oagmjl bapamgo luhjdb ws luhb bwml hj uam oabbwjb hj lua bkuwwomwwf, gji luwqdu luhb cgb jwl g PAMY dwwi wnnwmlqjhly swm buwchjd wss uam rjwcoaida, gb luama cgb jw wja lw ohblaj lw uam, blhoo hl cgb dwwi nmgklhka lw bgy hl wpam) --yab, lugl b gewql lua mhdul ihblgjka--eql luaj H cwjiam cugl Oglhlqia wm Owjdhlqia H pa dwl lw? (Gohka ugi jw hiag cugl Oglhlqia cgb, wm Owjdhlqia ahluam, eql luwqdul luay cama jhka dmgji cwmib lw bgy.)"""
shortcipher1 = "Luama cgb jwluhjd bw PAMY mafgmrgeoa hj lugl; jwm ihi Gohka luhjr hl bw PAMY fqku wql ws"
#ciphertext from the second problem
ciphertext2="""Zzuq bgjaqp ue; izp gj btq kclqez jwz pou vaf ifuq ngts, mzj ew fujzvse cvzq tkrzp al yqy, fnv aqouel eat jmf aak, izp zym emsv btutx pmbvvvqp zf puy. Nv uqf zym rad, npa sgmm tus kpq sufl mpbzkq: nak etqt ym omsv ba fnv bia oeve, toj mxpkjb ndukpqd cra efgeluzm rb ftk nqzpun etqxv btq svzdksrsuzm nie, mtu kmxrvl fa nzu fa ifuq ut; rvp tk twgxj ewf iokpefgel ftk kmybzrbuat, scf ikeb uz, gel raxxwf fnv oaxjvv nuxu izp nza oaaebdk oe btq yruq ygevqd. Zzuq bgjaqp ue ismoe, izp zym kaaeoqez jwz fuf euenvl fa yvb agz zvfa zym iujv eadru ba ekvs rax kpq suclqz hzzp; nak pue lrbtqx nwgxj ewf xojbqz zf qf rui i xatx
eturv, nad nv eme bvzk ruel ar nza eat, rvp igj irdgzl ftgk aayk ztx xats
yumyb tmvgmz fu yqy mrjw, mzj gzqhkeb tuy twyutx jmoq. Ywiqbvz, mf rraf uz
nie mmimqp nv ataacl sa, lfz tq cfcxp tfb dqyk if tudm; mzj ra tq iruq fu kpq
iufl, tq svb ftk wwj, mtu pqmxu btq yruq sufl oaaeaqx. Hlb tq cra ftgesrgr
kw ftk wwj, mtu lup tfb mfzvubf nza xulv ie toj jdazymde nrl patv; aa fnv naj
yrqp, ’Eok cbat dg fmoc, izp efc iurc bdmbvt rmykmd.’ Eu ym emz uwiz, gel ftk
wwj nkxiz fu icz, mtu iime kpqk cvvf abvz efuts mzj jbazk jw cgots ftgk btqoi
pmux npuezcmp ut kpq ioel. Itke btqe tiyq zf btq bztxmmv, btq yfv rarcwiqj kpq
ruo’a oaaeaqx, gel iuzywgf rfwwutx inaak puy cvvf fu kpq enrjnk oev mzj imefku btqxv ixx tzotf gk pue kraq. Ut kpq yuivuzm tiyq zym rad romut rvp ykk puy gj
pq igj jqsoevuzm yqe vulzzqe, rvp egzl, ’Sa ykzmumyb raxnidp, zztx kul kayk kw
m ogjbxq, hvnadk npuon cqq m cywxq ziwab uw aaxjzmde lraf mycmqb gel ezuiqzs: zrsq zu ewfuiv wr fnvu, ngz xw uzzf btq irafxk rvp bgja az gel az zztx kul
kayk kw m dufu, itkim ftk xwxpke judj jqfe oe i iauumz ogxm; oxujm nk ok afmtua
m nkrcfullt sarumz ogxm; ngz uw zaz kzk fu kiwq zym nuxu wgf uw btq yyinne tisq
gel bgz zb uzzf btq nrvpeudm azk, fbtqxnqeq efc iurc zqbkeb uf.’ Zymz fnv naj ykzqfiymp aak pue zrqx mmrqz, mtu btq efczs srv emz yqyekcn pace, izp gnik fnvg iqtk whqx jbaoq rvp ezfvq foct ftkzz tmoi etuyktqp oe btq czvp.Nkwwdq zym omyktq sgkm mxr nie my kpq ruo pmp yrqp: eu kpq eue eqzz zv mzj wwgzj kpq onrunqx npqdk kpq suclqz hzzp taeo uz g nwapke kmsk, rvp nkcwi ezfwp fnv oaxjvv ommv, izp zym ftxvm sarumz mvgtqe zyif tgu jqqt cwef cvzq xezvs orfaq ne zb. Ftke btaaxpf tkkw tusjmxr, ’Ok euxr sm m hkig pduct ftoeo fa hiqzs gnik eatp m roem nuxu qz fnza etgsjk ogxm’; ea nv wbqtvl ftk uwad gel faub paxj fn uf gel bgz zb uzzf
btq mftpqt tisq. Hlb ftk sqdp yvb gb ylkt m rfcp eiimmy zyif mrc btq yftpukia miubm, mzj kpqk zfww tod xduyfvqd gel omxiqqp nzu nqlfzq fnv suzm. Kpq zkob yaxeqzs zym oaaib emz kw vgjxm tus; rvp invv mxr nie tkrzp, uz jmzfkekqp nzu fa jzm, gzrvae tk jpagru jdutx btq qzvs fnv oaxjvv taxjm itotp oaacl dgt ra eiowbxk
gj btq czvp; mtu qr tk uqp fnza, tq cra fa nrdq fnv oaxjvv nuxu ouhke puy lfz
tuy fez. Eu ym eqz fcf attm yaxv wz toj ragxemk, eoxpuzm, rvp ut xzqmz umebgzz, itke wz m yllpqt yqe rxzmzp zym rad dmf tod, izp yrqp, ’Kul aqq tfe itgk pme nrxbqtvl az gtkagtk wr kulz zaz cqefkeqzs zf uk oulveqr. Z euxr jbuxr, ywiqbvz, fqrc gag nfe fa lzvp fnv oaxjvv taxjm, ur efc iurc la my Z jup efc. Kaa dcef
mf afdgzotf ue buxr pwg oudm fa zym omyktq invzq fnv padyv afmtua uz nza efgct: nk nza eujv euxr cqq fnv odaud nmez raxqkg izp yewdutx: bmwk remk zym taxjm cgovbxk, hlb nq ylzq fu gcf fnv wxp rviftkiv emjutq gvfv tus, rvp zuk btq mftpqt fvq fnrb ue icweq hp qf.’ Fnvv ftk jwz egk lait fv ftk wwj’e zrqx, mtu iime kpqk cvvf abvz efuts mzj jbazk kqxx zymud nrqd inzafxku qz fnv euzj."""
ciphertext2 = re.sub("\xe2\x80\x99", "", ciphertext2) #remove these characters
ciphertext2 = re.sub("\n", " ", ciphertext2)
shortcipher2 = "Zzuq bgjaqp ue; izp gj btq kclqez jwz pou vaf ifuq ngts, mzj ew fujzvse cvzq tkrzp al yqy, fnv aqouel eat jmf aak, izp zym emsv btutx pmbvvvqp zf puy."
#build dictionaries for conversion of letters to numbers
alpha = 'abcdefghijklmnopqrstuvwxyz'
alpha2Num = {}
num2Alpha = {}
for index in range(len(alpha)):
    alpha2Num[alpha[index]] = index
for index in range(len(alpha)):
    num2Alpha[index] = alpha[index]

#take a string and remove all 'illegal' characters
def formatString(someString):
    #remove whitespace
    someString = someString.replace(" ", "")
    #convert to lower case
    someString = someString.lower()
    #remove punctuation
    someString = someString.translate(None, string.punctuation)
    return someString
    

#Take a string, return a list
def encodePlaintext(someString):
    code = []
    for letter in someString:
        code.append(alpha2Num[letter])
    return code


#return a sorted list of tuples with the frequency count of each letter, someString is the string (as characters)
def getFrequencyCount(someString):
    frequency = {}
    someString = formatString(someString)
    totalLength = len(someString)
    for index in range(len(someString)):
        currentChar = someString[index]
        if not(frequency.has_key(currentChar)):
            frequency[currentChar] = 1
        else:
           currentVal = frequency[currentChar]
           currentVal += 1
           frequency[currentChar] = currentVal
    for key in frequency:
        intVal = frequency[key]
        proportion = intVal / float(totalLength) * 100
        proportion = round(proportion,4) 
        frequency[key] = proportion
    sorted_frequency = sorted(frequency.items(), key=operator.itemgetter(1))
    sorted_frequency.reverse()
    return sorted_frequency
                
#Takes an encoded list(i.e. numbers), returns string
def decodeText(codeString):
    newString = ""
    for value in codeString:
        newString += num2Alpha[value]
    return newString

#Performs affine shift on a string
def affineCipher(textString,alpha,beta):
    textString = formatString(textString)
    if not(gcd(alpha,26) == 1):
        raise ValueError('Alpha must be invertible [gcd(alpha,26) must equal 1.]')
    code = encodePlaintext(textString)
    for i in range(len(code)):
        code[i] = (code[i] * alpha + beta) % 26
    newString = decodeText(code)
    return newString

#Returns the multiplicative inverse of a number mod 26
def multInverse(a):
    if (gcd(a,26) == 1):
        x = 0
        while (a * x) % 26 != 1:
            x = x+1
        return x
    else:
        return 0

#Print all possiblities for an affine cipher
def bruteAffine():
    for i in range(26):
        if gcd(i,26) == 1:
            for j in range(26):
                print 'Alpha = ', str(i)  + 'Beta = ', str(j) + '\n'+ affineCipher(shortcipher1,i,j)

                


#Returns gcd of two numbers a and b
def gcd(a,b):
    if b == 0:
        return a
    else:
      return gcd(b, a % b)

#Finds probable key length of a vigenere cipher
def findKeyLength(cryptText):
    cryptText = formatString(cryptText)
    counts = {} #Empty dictionary that will store count of matches, where key=shift and value=matches
    textLength = len(cryptText)
    maxKeyLength = 10 #Just a guess that the key length is likely less than 10
    for i in range(1,maxKeyLength):
        count = 0
        cryptCopy = ""
        for j in range(textLength):
            cryptCopy += cryptText[(j+i) % textLength]
        #Check for matches
        for k in range(textLength):
            if cryptText[k] == cryptCopy[k]:
                count = count + 1
        counts[i] = count
    return counts

 #Will return groups of a ciphertext based on vigenere key length   
def getGroups(ciphertext, keylength):
    ciphertext = formatString(ciphertext)
    group = {}
    for i in range(keylength):
        count = i
        grouptext = ""
        while count < len(ciphertext):
            grouptext += ciphertext[count]
            count += keylength
        group[i] = grouptext
    return group

#Applies Vigenere shift, one key place at a time
#keyPosition is the place in the ciphertext to apply the key
#shift is the value shifted at that location
def Vigenere(cipherText, keyLength, shift, keyPosition):
    cipherText = formatString(cipherText)
    newCipher = ""
    for index in range(len(cipherText)):
        if index != keyPosition:
            newCipher += cipherText[index]
        else:
            newCipher += num2Alpha[(alpha2Num[cipherText[index]] + shift) % 26]
            keyPosition += keyLength
    return newCipher

            

            
